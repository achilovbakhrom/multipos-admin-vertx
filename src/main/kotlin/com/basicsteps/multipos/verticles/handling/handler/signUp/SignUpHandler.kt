package com.basicsteps.multipos.verticles.handling.handler.signUp

import com.basicsteps.multipos.config.CommonConstants
import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.isEmail
import com.basicsteps.multipos.core.isPhoneNumber
import com.basicsteps.multipos.core.model.exceptions.*
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.ErrorMessages
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.sign_up.*
import com.basicsteps.multipos.utils.GenratorUtils
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class SignUpHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {

    /**
     * SignUp handler
     * Saves SignUpMapper received from user
     */
    fun signUp(message: Message<String>) {
        val signUpMapper = JsonUtils.toPojo<SignUpMapper>(message.body())
        if (!signUpMapper.primaryPhone.isNullOrEmpty()) {
            if(!signUpMapper.primaryPhone!!.isPhoneNumber()) {
                message.reply(MultiPosResponse<Any>(null,
                        ErrorMessages.PHONE_NUMBER_IS_NOT_VALID.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
                return
            }
        }

        if (!signUpMapper.mail.isEmpty()) {
            if (!signUpMapper.mail.isEmail()) {
                message.reply(MultiPosResponse<Any>(null,
                        ErrorMessages.EMAIL_IS_NOT_VALID.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
                return
            }
        } else {
            message.reply(MultiPosResponse<Any>(null,
                    ErrorMessages.EMAIL_IS_EMPTY.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }
        signUpMapper.accessCode = GenratorUtils.generateRandomInt(100000, 999999)
        dbManager.signUpDao?.emailUnique(signUpMapper.mail)?.subscribe({result ->
            if (result) {
                dbManager.signUpDao
                        ?.save(signUpMapper, CommonConstants.ANONYMOUS)
                        ?.subscribe({
                            message.reply(MultiPosResponse<Any>(null, null,
                                    StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                        }, {error ->
                            error.printStackTrace()
                            when(error) {
                                is DataStoreException -> message.reply(MultiPosResponse<Any>(null, error.message,
                                        StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                                is WriteDbFailedException -> message.reply(MultiPosResponse<Any>(null,
                                        error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            }
                        })
            } else {
                message
                        .reply(MultiPosResponse<Any>(null,
                        ErrorMessages.EMAIL_IS_NOT_UNIQUE.value(), StatusMessages.ERROR.value(), HttpResponseStatus.CONFLICT.code()).toJson())
            }
        }, {error ->
            message.reply(MultiPosResponse<Any>(null,
                    error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
        })
    }

    /**
     * GetAccessCode
     * returns corresponding access code
     * requested by user with his mail
     */
    fun getAccessCode(message: Message<String>) {
        val mail = message.body()
        if (!mail.isEmpty()) {
            if (!mail.isEmail()) {
                message.reply(MultiPosResponse<Any>(null,
                        ErrorMessages.EMAIL_IS_NOT_VALID.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
                return
            }
        } else {
            message.reply(MultiPosResponse<Any>(null, ErrorMessages.EMAIL_IS_EMPTY.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }

        dbManager.signUpDao
                ?.getSignUpMapperByMail(mail)
                ?.subscribe({ item ->
                    message.reply(MultiPosResponse(AccessCodeResponse(item.accessCode), null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, { error ->

                    when(error) {
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is ReadDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is NotExistsException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()).toJson())
                        is FieldConflictsException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.CONFLICT.code()).toJson())
                    }
                })
    }

    /**
     * IsEmailExists
     * returns existence of email in db
     */
    fun isEmailExists(message: Message<String>) {
        val jsonObject = JsonObject(message.body())
        val email = jsonObject.getString("email")
        if (email.isNullOrEmpty()) {
            message.reply(MultiPosResponse<Any>(null,
                    ErrorMessages.EMAIL_IS_EMPTY.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }
        if (!email.isEmail()) {
            message.reply(MultiPosResponse<Any>(null,
                    ErrorMessages.EMAIL_IS_NOT_VALID.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }
        dbManager.signUpDao
                ?.emailUnique(email)
                ?.subscribe({unique ->
                    message.reply(MultiPosResponse(EmailCheckResponse(unique), null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, {error ->
                    when(error) {
                        is DataStoreException -> message.reply(MultiPosResponse<Any>(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is ReadDbFailedException -> message.reply(MultiPosResponse<Any>(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    /**
     * HandleConfirmation
     * confirms signUpMapper object data
     * by sending access code from user
     */
    fun handleConfirmation(message: Message<String>) {
        val confirmationMapper = JsonUtils.toPojo<ConfirmationMapper>(message.body())
        dbManager.signUpDao
            ?.accessCode(confirmationMapper.mail, confirmationMapper.accessCode)
            ?.subscribe({created ->
                if (created) {
                    message.reply(MultiPosResponse<Any>(null, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                } else {
                    message.reply(MultiPosResponse<Any>(null, ErrorMessages.WRONG_ACCESS_CODE.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
                }
            }, {error ->
                when(error) {
                    is NotExistsException -> message.reply(MultiPosResponse<Any>(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()).toJson())
                    is FieldConflictsException -> message.reply(MultiPosResponse<Any>(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.CONFLICT.code()).toJson())
                    is ReadDbFailedException -> message.reply(MultiPosResponse<Any>(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    is DataStoreException -> message.reply(MultiPosResponse<Any>(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    is DeleteDbFailedException -> message.reply(MultiPosResponse<Any>(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                }
            })
    }
}