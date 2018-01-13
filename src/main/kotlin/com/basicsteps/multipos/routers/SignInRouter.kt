package com.basicsteps.multipos.routers

import com.basicsteps.multipos.core.putBrowserHeaders
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.event_bus_channels.SignInHandlerChannel
import com.basicsteps.multipos.event_bus_channels.SignUpHandlerChannel
import com.basicsteps.multipos.model.sign_in.SignInResponseMapper
import com.basicsteps.multipos.model.sign_in.VerificationMapper
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.eventbus.Message
import io.vertx.ext.auth.oauth2.impl.AccessTokenImpl
import io.vertx.ext.web.RoutingContext
import java.util.*

class SignInRouter(val vertx: Vertx) {

    fun signIn(routingContext: RoutingContext) {
        print("test")
        vertx.eventBus().send(SignInHandlerChannel.SIGN_IN.value(), routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
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

    fun verification(routingContext: RoutingContext) {
        val map = (routingContext.user() as AccessTokenImpl).accessToken().map
        val username = map.get("preferred_username").toString()
        vertx.eventBus().send(SignInHandlerChannel.VERIFICATION.value(), username, { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val result = JsonUtils.toPojo<MultiPosResponse<VerificationMapper>>(reply.result().body().toString())
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