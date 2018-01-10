package com.basicsteps.multipos.verticles

import com.basicsteps.multipos.core.AbstractDbVerticle
import com.basicsteps.multipos.core.BusResponse
import com.basicsteps.multipos.core.exceptions.FieldConflictsException
import com.basicsteps.multipos.core.exceptions.NotExistsException
import com.basicsteps.multipos.core.exceptions.ReadDbFailedException
import com.basicsteps.multipos.core.exceptions.WriteDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.event_bus_channels.SignUpHandlerChannel
import com.basicsteps.multipos.managers.db.DbManager
import com.basicsteps.multipos.model.sign_up.*
import com.basicsteps.multipos.utils.JsonUtils
import com.basicsteps.multipos.utils.ValidationUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.eventbus.Message

class SignUpHandler(dbManager: DbManager) : AbstractDbVerticle(dbManager) {



    override fun start() {
        super.start()
        initConsumers()
    }

    private fun initConsumers() {
        vertx.eventBus().consumer<String>(SignUpHandlerChannel.SIGN_UP.value(), {message: Message<String>? -> this.handleSignUp(message!!) })
        vertx.eventBus().consumer<String>(SignUpHandlerChannel.CONFIRM_ACCESS_CODE.value(), {message: Message<String>? -> this.handleConfirmation(message!!) })
        vertx.eventBus().consumer<String>(SignUpHandlerChannel.IS_EMAIL_UNIQUE.value(), {message: Message<String>? -> this.isEmailExists(message!!)})
        vertx.eventBus().consumer<String>(SignUpHandlerChannel.GET_ACCESS_CODE.value(), {message: Message<String>? -> this.getAccessCode(message!!)})
    }


    // will be delete later
    private fun getAccessCode(message: Message<String>) {
        val mail = JsonUtils.toPojo<MailMapper>(message.body()).mail
        dbManager.signUpProtocol
                ?.getSignUpMapperByMail(mail)
                ?.subscribe({ item ->
                    message.reply(MultiPosResponse(AccessCodeResponse(item.accessCode), null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, { error ->
                    if (error is NotExistsException) {
                        message.reply(MultiPosResponse<Any>(null, "Wrong email address!!!", "Error", HttpResponseStatus.NOT_FOUND.code()).toJson())
                    }
                    if (error is FieldConflictsException) {
                        message.reply(MultiPosResponse<Any>(null, "There is more than two email!!!", "Error", HttpResponseStatus.CONFLICT.code()).toJson())
                    }
                    if (error is ReadDbFailedException) {
                        message.reply(MultiPosResponse<Any>(null, "Read from database failed!!!", "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    private fun handleSignUp(message: Message<String>) {

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

        dbManager
                .signUpProtocol
                ?.createSignUpMapper(signUpMapper)
                ?.subscribe({id ->
                    message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, {error ->
                    if (error is FieldConflictsException)
                        message.reply(MultiPosResponse<Any>(null, "Wrong email!!!", "Error", HttpResponseStatus.CONFLICT.code()).toJson())
                    if (error is WriteDbFailedException) {
                        message.reply(MultiPosResponse<Any>(null, "Write to database failed!!!", "Error", HttpResponseStatus.CONFLICT.code()).toJson())
                    }
                })
    }

    fun isEmailExists(message: Message<String>) {
        val email = message.body()
        dbManager.signUpProtocol
                ?.emailUnique(email)
                ?.subscribe({unique ->
                    message.reply(MultiPosResponse(EmailCheckResponse(unique), null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, {error ->
                    if (error is ReadDbFailedException) {
                        message.reply(MultiPosResponse<Any>(null, "Read from database failed!!!", "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    private fun handleConfirmation(message: Message<String>) {
        val confirmationMapper = JsonUtils.toPojo<ConfirmationMapper>(message.body())
        dbManager.signUpProtocol
                ?.accessCode(confirmationMapper.mail, confirmationMapper.accessCode)
                ?.subscribe({created ->
                    if (created) {
                        message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                    } else {
                        message.reply(MultiPosResponse<Any>(null, "Wrong access code!!!", "Error", HttpResponseStatus.BAD_REQUEST.code()).toJson())
                    }
                }, {error ->
                    if (error is NotExistsException) {

                        message.reply(MultiPosResponse<Any>(null, "Wrong email!!!", "Error", HttpResponseStatus.NOT_FOUND.code()).toJson())
                    }
                    if (error is FieldConflictsException) {
                        message.reply(MultiPosResponse<Any>(null, "There is more than two email!!!", "Error", HttpResponseStatus.CONFLICT.code()).toJson())
                    }
                    if (error is ReadDbFailedException) {
                        message.reply(MultiPosResponse<Any>(null, "Read from database failed!!!", "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    override fun stop() {
        super.stop()

    }
}