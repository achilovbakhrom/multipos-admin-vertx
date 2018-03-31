package com.basicsteps.multipos.core

import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.ext.web.RoutingContext

abstract class BaseRouter(val vertx: Vertx) {

    fun <T> processResult(reply: AsyncResult<Message<String>>, routingContext: RoutingContext) {
        if (reply.succeeded()) {
            val result = JsonUtils.toPojo<MultiPosResponse<T>>(reply.result().body())
            val statusCode = result.code!!
            if (statusCode < 400) {
                routingContext
                        .response()
                        .putBrowserHeaders()
                        .setStatusCode(statusCode)
                        .end(result.toJson())

            } else {
                routingContext.fail(statusCode)
            }

        } else{
            routingContext.
                    fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())

        }
    }

}