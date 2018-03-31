package com.basicsteps.multipos.verticles.routing.router

import com.basicsteps.multipos.core.BaseRouter
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.event_bus_channels.CompanyHandlerChannel
import com.basicsteps.multipos.event_bus_channels.SignUpHandlerChannel
import com.basicsteps.multipos.verticles.handling.handler.common.UploadHandler
import com.google.gson.Gson
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.ext.auth.oauth2.impl.OAuth2TokenImpl
import io.vertx.ext.web.RoutingContext

class CompanyRouter (vertx: Vertx) : BaseRouter(vertx) {

    fun getCompaniesByUsername(routingContext: RoutingContext) {
        val email = (routingContext.user() as OAuth2TokenImpl).accessToken().getString("email")
        val req = MultiposRequest<String>()
        req.data = email
        vertx.eventBus().send(CompanyHandlerChannel.GET_COMPANIES_BY_USERNAME.value(), req.toJson(), { reply: AsyncResult<Message<String>> ->
            processResult<Any>(reply, routingContext)
        })
    }

//    fun signUp(routingContext: RoutingContext) {
//        vertx.eventBus().send(SignUpHandlerChannel.SIGN_UP.value(), routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
//            processResult<Any>(reply, routingContext)
//        })
//    }
//
//    fun confirmAccessCode(routingContext: RoutingContext) {
//        vertx.eventBus().send(SignUpHandlerChannel.CONFIRM_ACCESS_CODE.value(), routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
//            processResult<Any>(reply, routingContext)
//        })
//    }
//
//    fun isEmailExists(routingContext: RoutingContext) {
//        vertx.eventBus().send(SignUpHandlerChannel.IS_EMAIL_UNIQUE.value(), routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
//            processResult<Boolean>(reply, routingContext)
//        })
//    }
//
//    fun getAccessCode(routingContext: RoutingContext) {
//        vertx.eventBus().send(SignUpHandlerChannel.GET_ACCESS_CODE.value(), routingContext.body.toString(), { reply: AsyncResult<Message<String>> ->
//            processResult<Int>(reply, routingContext)
//        })
//    }
//
//    fun uploadAvatar(routingContext: RoutingContext) {
//        UploadHandler.uploadAvatar(vertx, routingContext)
//    }

}