package com.basicsteps.multipos.verticles.handling.handler.config

import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.entities.Company
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message

class CompanyHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {

    fun createCompany(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Company>>(message.body().toString())
        dbManager
                .companyDao
                ?.save(request.data!!, request.userId!!)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                }, {error ->

                })
    }

    fun addUserToCompany(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
        dbManager
                .companyDao
                ?.findById(request.companyId!!)
                ?.doOnNext({ company -> company.ownersId.add(request.userId!!) })
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

    fun getComaniesByUserId(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
        dbManager
                .companyDao
                ?.findAll()
                ?.filter({companies ->
                    var result = false
                    for (company in companies) {
                        result = company.ownersId.contains(request.data)
                        if (result) break
                    }
                    result
                })
                ?.subscribe({filteredCompanies ->
                    message.reply(MultiPosResponse<List<Company>>(filteredCompanies, null, "OK", HttpResponseStatus.OK.code()))
                }, {error -> })
    }




}