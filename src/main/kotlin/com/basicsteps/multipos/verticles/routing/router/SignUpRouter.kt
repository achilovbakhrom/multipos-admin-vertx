package com.basicsteps.multipos.verticles.routing.router

import com.basicsteps.multipos.core.BaseRouter
import com.basicsteps.multipos.event_bus_channels.SignUpHandlerChannel
import com.basicsteps.multipos.verticles.handling.handler.common.UploadHandler
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.ext.web.RoutingContext

class SignUpRouter (vertx: Vertx) : BaseRouter(vertx) {

    fun signUp(routingContext: RoutingContext) {
        vertx.eventBus().send(SignUpHandlerChannel.SIGN_UP.value(), routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
            processResult<Any>(reply, routingContext)
        })
    }

    fun confirmAccessCode(routingContext: RoutingContext) {
        vertx.eventBus().send(SignUpHandlerChannel.CONFIRM_ACCESS_CODE.value(), routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
            processResult<Any>(reply, routingContext)
        })
    }

    fun isEmailExists(routingContext: RoutingContext) {
        vertx.eventBus().send(SignUpHandlerChannel.IS_EMAIL_UNIQUE.value(), routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
            processResult<Boolean>(reply, routingContext)
        })
    }

    fun getAccessCode(routingContext: RoutingContext) {

        vertx.eventBus().send(SignUpHandlerChannel.GET_ACCESS_CODE.value(), routingContext.queryParams().get("email"), { reply: AsyncResult<Message<String>> ->
            processResult<Int>(reply, routingContext)
        })
    }

    fun uploadAvatar(routingContext: RoutingContext) {
        UploadHandler.uploadAvatar(vertx, routingContext)
    }

}