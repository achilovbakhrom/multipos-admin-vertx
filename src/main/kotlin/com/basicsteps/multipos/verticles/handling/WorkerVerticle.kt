package com.basicsteps.multipos.verticles.handling

import com.basicsteps.multipos.config.CommonConstants
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.event_bus_channels.ConfigHandlerChannel
import com.basicsteps.multipos.event_bus_channels.SignInHandlerChannel
import com.basicsteps.multipos.event_bus_channels.SignUpHandlerChannel
import com.basicsteps.multipos.verticles.handling.handler.config.ConfigHandler
import com.basicsteps.multipos.verticles.handling.handler.signIn.SignInHandler
import com.basicsteps.multipos.verticles.handling.handler.signUp.SignUpHandler
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.Message

class WorkerVerticle : AbstractVerticle() {

    var dbManager: DbManager? = null

    var signUpHandler: SignUpHandler? = null
    var signInHandler: SignInHandler? = null
    var configHandler: ConfigHandler? = null
    override fun start() {
        super.start()
        dbManager = DbManager(vertx)
        initHandlers()
        initConsumers()
    }

    private fun initHandlers() {
        signUpHandler = SignUpHandler(vertx, dbManager!!)
        signInHandler = SignInHandler(vertx, dbManager!!)
        configHandler = ConfigHandler(vertx, dbManager!!)
    }

    private fun initConsumers() {
        //TEnant switcher
        vertx.eventBus().consumer<String>(CommonConstants.SWITCH_TENANT, {message -> dbManager?.setMongoClientByTenantId(message.body().toString())})
        //Sing UP
        vertx.eventBus().consumer<String>(SignUpHandlerChannel.SIGN_UP.value(), { message -> this.createSignUp(message!!) })
        vertx.eventBus().consumer<String>(SignUpHandlerChannel.CONFIRM_ACCESS_CODE.value(), { message -> this.handleConfirmation(message!!) })
        vertx.eventBus().consumer<String>(SignUpHandlerChannel.IS_EMAIL_UNIQUE.value(), { message -> this.isEmailExists(message!!)})
        vertx.eventBus().consumer<String>(SignUpHandlerChannel.GET_ACCESS_CODE.value(), { message -> this.getAccessCode(message!!)})

        //Sign In
        vertx.eventBus().consumer<String>(SignInHandlerChannel.SIGN_IN.value(), { message -> signInHandler?.signInHandler(message)})
        vertx.eventBus().consumer<String>(SignInHandlerChannel.VERIFICATION.value(), { message -> signInHandler?.verification(message)})
        vertx.eventBus().consumer<String>(SignInHandlerChannel.REFRESH_ACCESS_TOKEN.value(), {message -> signInHandler?.refreshAccessToken(message)})

        // <----------------------------- protected api --------------------------->

        //configuration
        vertx.eventBus().consumer<String>(ConfigHandlerChannel.GET_UNIT_CATEGORY.value(), { message -> configHandler?.getUnitCategories(message) })
        vertx.eventBus().consumer<String>(ConfigHandlerChannel.GET_UNIT_CATEGORY.value(), {message -> configHandler?.getUnitCategories(message)})
        vertx.eventBus().consumer<String>(ConfigHandlerChannel.GET_UNIT_CATEGORY_WITH_UNITS.value(), {message ->
            configHandler?.getUnitCategoriesWithUnits(message)
        })
    }

    private fun createSignUp(message: Message<String>) { signUpHandler?.signUp(message) }
    private fun getAccessCode(message: Message<String>) { signUpHandler?.getAccessCode(message) }
    private fun isEmailExists(message: Message<String>) { signUpHandler?.isEmailExists(message) }
    private fun handleConfirmation(message: Message<String>) { signUpHandler?.handleConfirmation(message) }

}