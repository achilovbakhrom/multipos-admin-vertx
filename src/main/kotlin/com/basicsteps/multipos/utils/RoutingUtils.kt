package com.basicsteps.multipos.utils

import com.basicsteps.multipos.core.putBrowserHeaders
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.event_bus_channels.SignInHandlerChannel
import com.basicsteps.multipos.model.sign_in.SignInResponseMapper
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.ext.web.RoutingContext

object RoutingUtils {
    fun route(vertx: Vertx, routingContext: RoutingContext, channelName: String, body: String? = null) {
        vertx.eventBus().send(channelName, body, { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val result = JsonUtils.toPojo<MultiPosResponse<SignInResponseMapper>>(reply.result().body())
                routingContext
                        .response()
                        .putBrowserHeaders()
                        .setStatusCode(result.code!!)
                        .end(result.toJson())
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