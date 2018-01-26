package com.basicsteps.multipos.verticles.handling.handler.signUp

import com.basicsteps.multipos.config.CommonConstants
import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.*
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.sign_up.*
import com.basicsteps.multipos.utils.ConfigUtils
import com.basicsteps.multipos.utils.GenratorUtils
import com.basicsteps.multipos.utils.JsonUtils
import com.basicsteps.multipos.utils.ValidationUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message


class SignUpHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {

    fun signUp(message: Message<String>) {
        val signUpMapper = JsonUtils.toPojo<SignUpMapper>(message.body())
        val primaryPhone = signUpMapper.primaryPhone

        if (!ValidationUtils.phoneNumber(primaryPhone)) {
            message.reply(MultiPosResponse<Any>(null, "Phone number is not valid!!!", "Error", HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }

        val eMail = signUpMapper .mail
        if (!ValidationUtils.email(eMail)) {
            message.reply(MultiPosResponse<Any>(null, "Email is not valid or null!!!", "Error", HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }

        signUpMapper.accessCode = GenratorUtils.generateRandomInt(100000, 999999)
        dbManager.signUpDao?.emailUnique(signUpMapper.mail)?.subscribe({result ->
            if (result) {
                dbManager.signUpDao
                        ?.save(signUpMapper, CommonConstants.ANONYMOUS)
                        ?.subscribe({item ->
                            message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                        }, {error ->
                            error.printStackTrace()
                            when(error) {
                                is DataStoreException -> message.reply(MultiPosResponse<Any>(null, error.message, "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                                is WriteDbFailedException -> message.reply(MultiPosResponse<Any>(null, error.message, "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            }

                        })
            } else {
                message.reply(MultiPosResponse<Any>(null, "${signUpMapper.mail} is not unique!!!", "Error", HttpResponseStatus.CONFLICT.code()).toJson())
            }
        }, {error ->
            message.reply(MultiPosResponse<Any>(null, "Error while read from DB!!!", "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
        })
    }

    fun getAccessCode(message: Message<String>) {
        val mail = JsonUtils.toPojo<MailMapper>(message.body()).mail
        if (!ValidationUtils.email(mail)) {
            message.reply(MultiPosResponse<Any>(null, "Email is not valid or null!!!", "Error", HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }
        dbManager.signUpDao
                ?.getSignUpMapperByMail(mail)
                ?.subscribe({ item ->
                    message.reply(MultiPosResponse(AccessCodeResponse(item.accessCode), null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, { error ->
                    when(error) {
                        is DataStoreException -> MultiPosResponse(null, error.message, "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()
                        is ReadDbFailedException -> MultiPosResponse(null, error.message, "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()
                        is NotExistsException -> MultiPosResponse(null, error.message, "Error", HttpResponseStatus.NOT_FOUND.code()).toJson()
                        is FieldConflictsException -> MultiPosResponse(null, error.message, "Error", HttpResponseStatus.CONFLICT.code()).toJson()
                    }
                })
    }

    fun isEmailExists(message: Message<String>) {
        val email = message.body()
        if (!ValidationUtils.email(email)) {
            message.reply(MultiPosResponse<Any>(null, "Email is not valid or null!!!", "Error", HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }
        dbManager.signUpDao
                ?.emailUnique(email)
                ?.subscribe({unique ->
                    message.reply(MultiPosResponse(EmailCheckResponse(unique), null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, {error ->
                    when(error) {
                        is DataStoreException -> message.reply(MultiPosResponse<Any>(null, error.message, "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is ReadDbFailedException -> message.reply(MultiPosResponse<Any>(null, error.message, "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    fun handleConfirmation(message: Message<String>) {
        val confirmationMapper = JsonUtils.toPojo<ConfirmationMapper>(message.body())
        dbManager.signUpDao
            ?.accessCode(confirmationMapper.mail, confirmationMapper.accessCode)
            ?.subscribe({created ->
                if (created) {
                    dbManager.getTenantIdByEmail(confirmationMapper.mail).subscribe({tenantId ->
                        dbManager.setTenantId(tenantId).subscribe()
                        //TODO FOr Currencies also
                        vertx.executeBlocking<String>({handler ->
                            ConfigUtils
                                    .getUnitCategories(vertx)
                                    .blockingFirst()
                                    .forEach({item -> dbManager.unitCategoryEntityDao?.save(item, CommonConstants.ANONYMOUS)?.subscribe() })
                            ConfigUtils.getUnits(vertx).blockingFirst().forEach({item -> dbManager.unitEntityDao?.save(item, CommonConstants.ANONYMOUS)?.subscribe() })
                            ConfigUtils.getCurrencies(vertx).subscribe({result ->
                                dbManager
                                        .currencyDao
                                        ?.saveAll(result, CommonConstants.ANONYMOUS)
                                        ?.subscribe({ result ->
                                            print("saved")
                                        }, {error ->
                                            print("error")
                                        })
                            })
                            message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                        }, {})
                    }, { error ->
                        error.printStackTrace()
                        when(error) {
                            is TenantNotFoundException -> message.reply(MultiPosResponse<Any>(null, error.message, "Error", HttpResponseStatus.NOT_FOUND.code()).toJson())
                        }
                    })
                } else {
                    message.reply(MultiPosResponse<Any>(null, "Wrong access code!!!", "Error", HttpResponseStatus.BAD_REQUEST.code()).toJson())
                }
            }, {error ->
                when(error) {
                    is NotExistsException -> message.reply(MultiPosResponse<Any>(null, error.message, "Error", HttpResponseStatus.NOT_FOUND.code()).toJson())
                    is FieldConflictsException -> message.reply(MultiPosResponse<Any>(null, error.message, "Error", HttpResponseStatus.CONFLICT.code()).toJson())
                    is ReadDbFailedException -> message.reply(MultiPosResponse<Any>(null, error.message, "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    is DataStoreException -> message.reply(MultiPosResponse<Any>(null, error.message, "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    is DeleteDbFailedException -> message.reply(MultiPosResponse<Any>(null, error.message, "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                }
            })
    }
}