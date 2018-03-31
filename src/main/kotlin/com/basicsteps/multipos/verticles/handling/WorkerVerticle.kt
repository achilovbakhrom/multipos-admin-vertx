package com.basicsteps.multipos.verticles.handling

import com.basicsteps.multipos.config.CommonConstants
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.event_bus_channels.CompanyHandlerChannel
import com.basicsteps.multipos.event_bus_channels.ConfigHandlerChannel
import com.basicsteps.multipos.event_bus_channels.SignInHandlerChannel
import com.basicsteps.multipos.event_bus_channels.SignUpHandlerChannel
import com.basicsteps.multipos.verticles.handling.handler.company.CompanyHandler
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
    var companyHandler: CompanyHandler? = null

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
        companyHandler = CompanyHandler(vertx, dbManager!!)
    }

    override fun stop() {
        super.stop()
        dbManager?.close()
    }

    private fun initConsumers() {

        //Tenant switcher
        vertx.eventBus().consumer<String>(CommonConstants.SWITCH_TENANT, {message -> dbManager?.setMongoClientByTenantId(message.body().toString())})

        //Sing Up
        vertx.eventBus().consumer<String>(SignUpHandlerChannel.SIGN_UP.value(), { message -> this.createSignUp(message!!) })
        vertx.eventBus().consumer<String>(SignUpHandlerChannel.CONFIRM_ACCESS_CODE.value(), { message -> this.handleConfirmation(message!!) })
        vertx.eventBus().consumer<String>(SignUpHandlerChannel.IS_EMAIL_UNIQUE.value(), { message -> this.isEmailExists(message!!)})
        vertx.eventBus().consumer<String>(SignUpHandlerChannel.GET_ACCESS_CODE.value(), { message -> this.getAccessCode(message!!)})

        //Sign In
        vertx.eventBus().consumer<String>(SignInHandlerChannel.SIGN_IN.value(), { message -> signInHandler?.signInHandler(message)})
        vertx.eventBus().consumer<String>(SignInHandlerChannel.VERIFICATION.value(), { message -> signInHandler?.verification(message)})
        vertx.eventBus().consumer<String>(SignInHandlerChannel.REFRESH_ACCESS_TOKEN.value(), {message -> signInHandler?.refreshAccessToken(message)})

        // <----------------------------- protected api with tokens --------------------------->
        vertx.eventBus().consumer<String>(SignInHandlerChannel.ADD_COMPANY.value(), {message -> companyHandler?.createCompany(message)})

        // Company
        vertx.eventBus().consumer<String>(CompanyHandlerChannel.GET_COMPANIES_BY_USERNAME.value(), { message -> companyHandler?.getCompaniesByUserId(message)})

        // <----------------------------- protected api with tokens and tenancy header --------------------------->

        //configuration
        //units
        vertx.eventBus().consumer<String>(ConfigHandlerChannel.GET_UNITS.value(), { message -> configHandler?.getUnits(message) })
        vertx.eventBus().consumer<String>(ConfigHandlerChannel.GET_UNIT_CATEGORY.value(), {message -> configHandler?.getUnitCategories(message)})
        vertx.eventBus().consumer<String>(ConfigHandlerChannel.ACTIVATE_UNIT.value(), {message -> configHandler?.activateUnit(message) })
        vertx.eventBus().consumer<String>(ConfigHandlerChannel.DEACTIVATE_UNIT.value(), {message -> configHandler?.deactivateUnit(message) })

        //POS & Stock
        vertx.eventBus().consumer<String>(ConfigHandlerChannel.ADD_POS.value(), { message -> configHandler?.addPOS(message) })
        vertx.eventBus().consumer<String>(ConfigHandlerChannel.ADD_STOCK.value(), { message -> configHandler?.addStock(message) })
        vertx.eventBus().consumer<String>(ConfigHandlerChannel.LINK_POS_STOCK.value(), { message -> configHandler?.linkPOSStock(message) })

        //Currency
        vertx.eventBus().consumer<String>(ConfigHandlerChannel.GET_CURRENCY_LIST.value(), { message -> configHandler?.getCurrencies(message) })

        //Account
        vertx.eventBus().consumer<String>(ConfigHandlerChannel.ADD_ACCOUNT.value(), { message -> configHandler?.createAccount(message) })


    }

    private fun createSignUp(message: Message<String>) { signUpHandler?.signUp(message) }
    private fun getAccessCode(message: Message<String>) { signUpHandler?.getAccessCode(message) }
    private fun isEmailExists(message: Message<String>) { signUpHandler?.isEmailExists(message) }
    private fun handleConfirmation(message: Message<String>) { signUpHandler?.handleConfirmation(message) }

}