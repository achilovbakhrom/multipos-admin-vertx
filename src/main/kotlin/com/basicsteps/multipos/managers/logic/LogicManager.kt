package com.basicsteps.multipos.managers.logic

import com.basicsteps.multipos.managers.db.DbManager
import com.basicsteps.multipos.managers.db.signUp.SignUpProtocol
import com.basicsteps.multipos.managers.db.signUp.SignUpProtocolImpl
import com.basicsteps.multipos.managers.logic.signUp.SignUpLogicProtocol
import com.basicsteps.multipos.managers.logic.signUp.SignUpLogicProtocolImpl
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.ext.web.RoutingContext

class LogicManager {
    var signUpLogic: SignUpLogicProtocol? = null
    constructor(vertx: Vertx) {
        this.signUpLogic = SignUpLogicProtocolImpl(DbManager(vertx))
    }

    fun signUpHandler(message: Message<String>) {
        signUpLogic?.signUp(message)
    }

    fun signInHandler(message: Message<String>) {
        signUpLogic?.signIn(message)
    }


    fun confirmAccessCodeHandler(message: Message<String>) {
        signUpLogic?.confirmAccesCode(message)
    }

    fun isEmailExistsHandler(message: Message<String>) {
        signUpLogic?.isEmailUnique(message)
    }


}