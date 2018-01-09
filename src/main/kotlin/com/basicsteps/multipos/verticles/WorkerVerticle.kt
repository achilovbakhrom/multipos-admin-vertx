package com.basicsteps.multipos.verticles


import com.basicsteps.multipos.config.EventBusHandlerChannels
import com.basicsteps.multipos.managers.logic.LogicManager
import io.vertx.core.AbstractVerticle


class WorkerVerticle : AbstractVerticle() {

    var logicManager: LogicManager? = null

    override fun start() {
        super.start()
        logicManager = LogicManager(vertx)
        initSubscribers()
    }

    private fun initSubscribers() {
        vertx.eventBus().consumer<String>(EventBusHandlerChannels.SIGN_UP.channel, { message -> logicManager?.signUpHandler(message) })
        vertx.eventBus().consumer<String>(EventBusHandlerChannels.CONFIRM_ACCESS_CODE.channel, { message -> logicManager?.confirmAccessCodeHandler(message) })
        vertx.eventBus().consumer<String>(EventBusHandlerChannels.IS_EMAIL_UNIQUE.channel, { message -> logicManager?.isEmailExistsHandler(message) })

    }

}