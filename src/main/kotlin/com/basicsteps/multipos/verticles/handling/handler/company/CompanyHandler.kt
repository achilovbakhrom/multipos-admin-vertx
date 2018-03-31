package com.basicsteps.multipos.verticles.handling.handler.company

import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.DataNotFoundException
import com.basicsteps.multipos.core.model.exceptions.FieldConflictsException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.core.model.exceptions.WriteDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.*
import com.basicsteps.multipos.utils.ConfigUtils
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.Observable
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message

class CompanyHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {

    fun createCompany(message: Message<String>) {
        var tempRequest = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())

        val request = MultiposRequest<Company>()
        request.userId = tempRequest.userId
        request.companyId = tempRequest.companyId
        request.data = JsonUtils.toPojo<Company>(tempRequest.data!!)

        val company = request.data
        if (company == null) {
            message.reply(
                    MultiPosResponse(DataNotFoundException(),
                            ErrorMessages.COMPANY_NOT_FOUND.value(),
                            StatusMessages.ERROR.value(),
                            HttpResponseStatus.BAD_REQUEST.code()
                    ).toJson()
            )
        } else {
            dbManager
                    .tenantsDao
                    ?.generateTenant()
                    ?.subscribe({item ->
                        company.tenantId = item
                        company.userId = request.userId
                        dbManager
                                .companyDao
                                ?.save(company, company.userId!!)
                                ?.subscribe({ company ->
                                    dbManager.setMongoClientByTenantId(item)
                                    prepareCompanyData(request.userId!!, item, company.id!!)
                                    message.reply(MultiPosResponse(company, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                                }, {error ->
                                    when (error) {
                                        is WriteDbFailedException -> {
                                            message.reply(MultiPosResponse(null, error.message,
                                                    StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                                        }
                                        is DataStoreException -> {
                                            message.reply(MultiPosResponse(null, error.message,
                                                    StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                                        }
                                    }
                                })


                    }, { error ->
                        when(error) {
                            is FieldConflictsException -> {message.reply(MultiPosResponse(null, ErrorMessages.COMPANY_IDENTIFIER_NOT_UNIQUE.value(),
                                    StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())}
                            is WriteDbFailedException -> {
                                message.reply(MultiPosResponse(null, error.message,
                                        StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            }
                            is DataStoreException -> {
                                message.reply(MultiPosResponse(null, error.message,
                                        StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            }
                            else -> {
                                message.reply(MultiPosResponse<Any>(null, ErrorMessages.UNKNOWN_ERROR.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
                            }
                        }
                    })
        }
    }

    private fun prepareCompanyData(userId: String, tenanntId: String, companyId: String) {
        ConfigUtils
                .getUnits(vertx)
                .flatMap({ unitEntities -> dbManager.unitEntityDao?.saveAll(unitEntities, userId) })
                .flatMap({ConfigUtils.getUnitCategories(vertx)})
                .flatMap({ unitCategories -> dbManager.unitCategoryEntityDao?.saveAll(unitCategories, userId) })
                .flatMap({ ConfigUtils.getCurrencies(vertx) })
                .flatMap({ currencies -> dbManager.currencyDao?.saveAll(currencies, userId) })
                .map({
                    val tenantsObject = Tenants()
                    tenantsObject.tenant = tenanntId
                    tenantsObject.companyIdentifier = companyId
                    tenantsObject
                })
                .flatMap({ tenant -> dbManager.tenantsDao?.save(tenant, userId)})
                .map({
                    val userCompanyRel = UserCompanyRel()
                    userCompanyRel.userName = userId
                    userCompanyRel.tenantId= tenanntId
                    userCompanyRel
                })
                .flatMap({ item -> dbManager.userCompanyRelDao?.save(item, userId) })
                .subscribe()
    }

    fun addUserToCompany(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
        dbManager
                .companyDao
                ?.findById(request.companyId!!)
                ?.doOnNext({ company ->/**company.ownersId.add(request.userId!!)*/ })
                ?.flatMap({ company -> dbManager.companyDao?.save(company, request.userId!!) })
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                }, { error ->

                })
    }

    fun trashCompany(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Company>>(message.body().toString())
        dbManager
                .companyDao
                ?.trash(request.data!!, request.userId!!)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                }, { error ->

                })
    }

    fun updateCompany(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Company>>(message.body().toString())
        dbManager
                .companyDao
                ?.update(request.data!!, request.userId!!)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                }, {error ->

                })
    }

    fun getCompaniesByUserId(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
        dbManager
                .companyDao
                ?.getCompanyByMail(request.data!!)
                ?.subscribe({ companies ->
                    message.reply(MultiPosResponse(companies, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, { error ->
                    when(error) {
                        is ReadDbFailedException -> {
                            message.reply(MultiPosResponse<Any>(null,
                                    ErrorMessages.EMAIL_IS_EMPTY.value(), StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        }
                        is Exception -> {
                            message.reply(MultiPosResponse<Any>(null,
                                    ErrorMessages.EMAIL_IS_EMPTY.value(), StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        }
                    }
                })
    }



}