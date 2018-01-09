package com.basicsteps.multipos.routers

import com.basicsteps.multipos.config.EventBusHandlerChannels
import com.basicsteps.multipos.core.LMResponse
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.ext.web.RoutingContext

class SignUpRouter (val vertx: Vertx) {

    fun signUp(routingContext: RoutingContext) {
        vertx.eventBus().send(EventBusHandlerChannels.SIGN_UP.channel, routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val result = JsonUtils.toPojo<LMResponse>(reply.result().body())
                routingContext.response().setStatusCode(result.status).end(result.message)
            } else{
                routingContext
                        .response()
                        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                        .end(reply.cause().toString())
            }
        })
    }


    fun confirmAccessCode(routingContext: RoutingContext) {
        vertx.eventBus().send(EventBusHandlerChannels.CONFIRM_ACCESS_CODE.channel, routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val result = JsonUtils.toPojo<LMResponse>(reply.result().body())
                routingContext.response().setStatusCode(result.status).end(result.message)
            } else{
                routingContext
                        .response()
                        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                        .end(reply.cause().toString())
            }

        })
    }

    fun isEmailExists(routingContext: RoutingContext) {
        vertx.eventBus().send(EventBusHandlerChannels.IS_EMAIL_UNIQUE.channel, routingContext.body, { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val result = JsonUtils.toPojo<LMResponse>(reply.result().body())
                routingContext.response().setStatusCode(result.status).end(result.message)
            } else{
                routingContext
                        .response()
                        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                        .end(reply.cause().toString())
            }
        })
    }

    fun signIn(routingContext: RoutingContext) {
        vertx.eventBus().send(EventBusHandlerChannels.SIGN_IN.channel, routingContext.body, { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val result = JsonUtils.toPojo<LMResponse>(reply.result().body())
                routingContext.response().setStatusCode(result.status).end(result.message)
            } else{
                routingContext
                        .response()
                        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                        .end(reply.cause().toString())
            }
        })
    }

}