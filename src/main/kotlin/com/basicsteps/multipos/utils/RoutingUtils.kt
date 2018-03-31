package com.basicsteps.multipos.utils

import com.basicsteps.multipos.config.CommonConstants
import com.basicsteps.multipos.core.putBrowserHeaders
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.event_bus_channels.SignInHandlerChannel
import com.basicsteps.multipos.model.sign_in.SignInResponseMapper
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.ext.auth.oauth2.AccessToken
import io.vertx.ext.auth.oauth2.impl.OAuth2TokenImpl
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

    inline fun <reified T> requestFromPathParams(routingContext: RoutingContext, pathParamName: String): MultiposRequest<T> {
        val request = MultiposRequest<T>()
        request.userId = (routingContext.user() as OAuth2TokenImpl).accessToken().getString("email")
        request.companyId = routingContext.request().headers().get(CommonConstants.HEADER_TENANT)
        request.data = routingContext.pathParam(pathParamName) as? T
        return request
    }

    inline fun <reified T> requestFromQueryParams(routingContext: RoutingContext, queryParamName: String): MultiposRequest<T> {
        val request = MultiposRequest<T>()
        request.userId = (routingContext.user() as OAuth2TokenImpl).accessToken().getString("email")
        request.companyId = routingContext.request().headers().get(CommonConstants.HEADER_TENANT)
        request.data = routingContext.queryParam(queryParamName) as? T
        return request
    }


}