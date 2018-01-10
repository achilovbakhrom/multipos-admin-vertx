package com.basicsteps.multipos.routers

import com.basicsteps.multipos.core.putBrowserHeaders
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.event_bus_channels.SignUpHandlerChannel
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.ext.web.RoutingContext

class SignUpRouter (val vertx: Vertx) {

    fun signUp(routingContext: RoutingContext) {
        vertx.eventBus().send(SignUpHandlerChannel.SIGN_UP.value(), routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val result = JsonUtils.toPojo<MultiPosResponse<Any>>(reply.result().body())
                routingContext.response()
                        .putBrowserHeaders()
                        .setStatusCode(result.code!!).end(result.toJson())
            } else{
                routingContext
                        .response()
                        .putBrowserHeaders()
                        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                        .end(reply.cause().toString())
            }
        })
    }

    fun confirmAccessCode(routingContext: RoutingContext) {
        vertx.eventBus().send(SignUpHandlerChannel.CONFIRM_ACCESS_CODE.value(), routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val result = JsonUtils.toPojo<MultiPosResponse<Any>>(reply.result().body())
                routingContext.response()
                        .putBrowserHeaders()
                        .setStatusCode(result.code!!).end(result.toJson())
            } else{
                routingContext
                        .response()
                        .putBrowserHeaders()
                        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                        .end(reply.cause().toString())
            }
        })
    }

    fun isEmailExists(routingContext: RoutingContext) {
        vertx.eventBus().send(SignUpHandlerChannel.IS_EMAIL_UNIQUE.value(), routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val result = JsonUtils.toPojo<MultiPosResponse<Boolean>>(reply.result().body())
                routingContext.response()
                        .putBrowserHeaders()
                        .setStatusCode(result.code!!).end(result.toJson())
            } else{
                routingContext
                        .response()
                        .putBrowserHeaders()
                        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                        .end(reply.cause().toString())
            }
        })
    }

    fun getAccessCode(routingContext: RoutingContext) {
        vertx.eventBus().send(SignUpHandlerChannel.GET_ACCESS_CODE.value(), routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val result = JsonUtils.toPojo<MultiPosResponse<Int>>(reply.result().body())
                routingContext.response()
                        .putBrowserHeaders()
                        .setStatusCode(result.code!!).end(result.toJson())
            } else{
                routingContext
                        .response()
                        .putBrowserHeaders()
                        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                        .end(reply.cause().toString())
            }
        })
    }
}