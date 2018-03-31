package com.basicsteps.multipos.verticles.routing.router

import com.basicsteps.multipos.core.BaseRouter
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.event_bus_channels.SignInHandlerChannel
import com.basicsteps.multipos.model.sign_in.SignInMapper
import com.basicsteps.multipos.model.sign_in.SignInResponseMapper
import com.basicsteps.multipos.model.sign_in.VerificationMapper
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.ext.auth.oauth2.AccessToken
import io.vertx.ext.auth.oauth2.impl.OAuth2TokenImpl
import io.vertx.ext.web.RoutingContext

class SignInRouter(vertx: Vertx) : BaseRouter(vertx) {

    fun addCompany(routingContext: RoutingContext) {
        val email = (routingContext.user() as OAuth2TokenImpl).accessToken().getString("email")
        val req = MultiposRequest<String>()
        req.userId = email
        req.data = routingContext.bodyAsString
        vertx.eventBus().send(SignInHandlerChannel.ADD_COMPANY.value(), req.toJson(), { reply: AsyncResult<Message<String>> ->
            processResult<SignInResponseMapper>(reply, routingContext)
        })
    }

    fun signIn(routingContext: RoutingContext) {
        vertx.eventBus().send(SignInHandlerChannel.SIGN_IN.value(), routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
            processResult<SignInMapper>(reply, routingContext)
        })
    }

    fun verification(routingContext: RoutingContext) {
        val map = (routingContext.user() as AccessToken).accessToken().map
        val username = map.get("preferred_username").toString()
        vertx.eventBus().send(SignInHandlerChannel.VERIFICATION.value(), username, { reply: AsyncResult<Message<String>> ->
            processResult<VerificationMapper>(reply, routingContext)
        })
    }

}