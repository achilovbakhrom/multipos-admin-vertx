package com.basicsteps.multipos.managers.logic.signUp

import io.vertx.core.eventbus.Message
import io.vertx.ext.web.RoutingContext

interface SignUpLogicProtocol {
    fun signUp(message: Message<String>)
    fun confirmAccesCode(message: Message<String>)
    fun isEmailUnique(message: Message<String>)
    fun signIn(message: Message<String>)
}