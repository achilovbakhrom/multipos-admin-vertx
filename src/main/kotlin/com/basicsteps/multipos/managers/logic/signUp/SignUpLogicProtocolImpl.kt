package com.basicsteps.multipos.managers.logic.signUp

import com.basicsteps.multipos.core.LMResponse
import com.basicsteps.multipos.core.DbException
import com.basicsteps.multipos.managers.db.DbManager
import com.basicsteps.multipos.model.sign_up.ConfirmationMapper
import com.basicsteps.multipos.model.sign_up.SignInMapper
import com.basicsteps.multipos.model.sign_up.SignUpMapper
import com.basicsteps.multipos.utils.JsonUtils
import com.basicsteps.multipos.utils.ValidationUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.schedulers.Schedulers
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.oauth2.providers.KeycloakAuth

class SignUpLogicProtocolImpl(val dbManager: DbManager) : SignUpLogicProtocol {

    override fun signIn(message: Message<String>) {
        var signInMapper = JsonUtils.toPojo<SignInMapper>(message.body())

    }


    override fun signUp(message: Message<String>) {
        val signUpObject = JsonUtils.toPojo<SignUpMapper>(message.body())
        val primaryPhone = signUpObject.primaryPhone
        if (primaryPhone != null && !ValidationUtils.phoneNumber(primaryPhone)) {
            message.reply(LMResponse(HttpResponseStatus.BAD_REQUEST.code(), "Phone number is not valid!!!"))
            return
        }

        val eMail = signUpObject.mail
        if (!ValidationUtils.email(eMail)) {
            message.reply(LMResponse(HttpResponseStatus.BAD_REQUEST.code(), "Email is not valid or null").toJson())
            return
        }
        dbManager
                .signUpProtocol
                ?.createSignUpMapper(signUpObject)
                ?.subscribe({id ->
                    message.reply(LMResponse(HttpResponseStatus.OK.code(), LMResponse.okMsg("id", id)).toJson())
                }, {error ->
                    if (error is DbException) {
                        when(error.code) {
                            DbException.SUCH_DATA_EXISTS_CODE -> message.reply(LMResponse(HttpResponseStatus.CONFLICT.code(),
                                    "Such mail already exists !!!").toJson())
                            DbException.DB_FAILED_CODE -> message.reply(LMResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(),
                                    "Error while writing to Db").toJson())
                        }
                    }
                })
    }

    override fun confirmAccesCode(message: Message<String>) {
        val confirmationMapper = JsonUtils.toPojo<ConfirmationMapper>(message.body())
        dbManager
                .signUpProtocol
                ?.accessCode(confirmationMapper.mail, confirmationMapper.accessCode)
                ?.subscribe({created ->
                    if (created) {
                        message.reply(LMResponse.okMsg("confirmation", "successful"))
                    } else {
                        message.reply(LMResponse.okMsg("confirmation", "unsuccess, cause does not match access code"))
                    }
                }, {error ->
                    if (error is DbException) {
                        when(error.code) {
                            DbException.SUCH_DATA_EXISTS_CODE -> message.reply(LMResponse(HttpResponseStatus.CONFLICT.code(),
                                    "Such mail already exists !!!").toJson())
                            DbException.DB_FAILED_CODE -> message.reply(LMResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(),
                                    "Error while writing to Db").toJson())
                        }
                    }
                })
    }

    override fun isEmailUnique(message: Message<String>) {
        val mail = message.body()
        dbManager
                .signUpProtocol
                ?.emailUnique(mail)
                ?.subscribe({exists ->
                    message.reply(LMResponse.okMsg("exists", if (exists) "true" else "false"))
                }, {error ->
                    if (error is DbException) {
                        message.reply(LMResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(),
                                "Error while reading from DB"))
                    }
                })
    }

}