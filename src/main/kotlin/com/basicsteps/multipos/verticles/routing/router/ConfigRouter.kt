package com.basicsteps.multipos.verticles.routing.router

import com.basicsteps.multipos.config.CommonConstants
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.event_bus_channels.ConfigHandlerChannel
import com.basicsteps.multipos.utils.RoutingUtils
import io.vertx.core.Vertx
import io.vertx.ext.auth.oauth2.impl.OAuth2TokenImpl
import io.vertx.ext.web.RoutingContext

/**
 * Configuration:
 *
 *  - Units
 *  - Unit categories
 *  - Currency
 *  - Account
 *  - Payment type
 *  - Vendor
 *  - Product
 *  - Combo
 *  - Matrix
 *
 */

class ConfigRouter(val vertx: Vertx) {

    // Units
    fun getUnits(routingContext: RoutingContext) { RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.GET_UNITS.value()) }
    fun getUnitCategories(routingContext: RoutingContext) { RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.GET_UNIT_CATEGORY.value()) }
    fun getUnitCategoriesWithUnits(routingContext: RoutingContext) { RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.GET_UNIT_CATEGORY_WITH_UNITS.value()) }
    fun activateUnits(routingContext: RoutingContext) { RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.GET_UNIT_CATEGORY_WITH_UNITS.value()) }
    fun activateUnit(routingContext: RoutingContext) {
        val request = RoutingUtils.requestFromPathParams<String>(routingContext, "unit_id")
        RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.ACTIVATE_UNIT.value(), request.toJson())
    }
    fun deactivateUnit(routingContext: RoutingContext) {
        val request = RoutingUtils.requestFromPathParams<String>(routingContext, "unit_id")
        RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.DEACTIVATE_UNIT.value(), request.toJson())
    }

    // ---------- Unit Category routes ---------
    fun addPOS(routingContext: RoutingContext) { RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.ADD_POS.value()) }
    fun addStock(routingContext: RoutingContext) { RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.ADD_STOCK.value()) }
    fun linkPOSStock(routingContext: RoutingContext) { RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.LINK_POS_STOCK.value()) }


    // ------------- End unit routes -------------

    // ------------- Currency routes --------------
    fun getCurrencies(routingContext: RoutingContext) {

        RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.GET_CURRENCY_LIST.value())
    }

    fun activateCurrencyList(routingContext: RoutingContext) {

    }

    fun activateCurrency(routingContext: RoutingContext) {

    }
    // -------------- End currency routes -----------

    // -------------- Account routes ----------------


    fun getAccountList(routingContext: RoutingContext) {
        RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.GET_ACCOUNT_LIST.value())
    }
    fun createAccount(routingContext: RoutingContext) {
        RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.ADD_ACCOUNT.value())
    }

    fun updateAccount(routingContext: RoutingContext) {
        RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.UPDATE_ACCOUNT.value())
    }

    fun getAccountById(routingContext: RoutingContext) {
        RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.GET_ACCOUNT_BY_ID.value())
    }

    fun removeAccounts(routingContext: RoutingContext) {
        RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.REMOVE_ACCOUNT.value())
    }

    fun removeAccount(routingContext: RoutingContext) {
        RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.REMOVE_ACCOUNT_LIST.value())
    }

    fun trashAccount(routingContext: RoutingContext) {
        RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.TRASH_ACCOUNT.value())
    }

    fun trashAccounts(routingContext: RoutingContext) {
        RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.TRASH_ACCOUNT_LIST.value())
    }
    // -------------- End account routes -------------


    // -------------- Payment type routes -------------------
    fun createPaymentType(routingContext: RoutingContext) {

    }

    fun updatePaymentType(routingContext: RoutingContext) {

    }

    fun getAllPaymentTypes(routingContext: RoutingContext) {

    }

    fun getPaymentTypeById(routingContext: RoutingContext) {

    }

    fun removePaymentType(routingContext: RoutingContext) {

    }

    fun removePaymentTypes(routingContext: RoutingContext) {

    }

    fun trashPaymentType(routingContext: RoutingContext) {

    }

    fun trashPaymentTypes(routingContext: RoutingContext) {

    }
    // ------------------- End Payment type routes ------------


    // ------------------- Vendor routes -------------------
    fun createVendor(routingContext: RoutingContext) {

    }

    fun updateVendor(routingContext: RoutingContext) {

    }

    fun getAllVendors(routingContext: RoutingContext) {

    }

    fun getVendorById(routingContext: RoutingContext) {

    }

    fun removeVendor(routingContext: RoutingContext) {

    }

    fun removeVendors(routingContext: RoutingContext) {

    }

    fun trashVendor(routingContext: RoutingContext) {

    }

    fun trashVendors(routingContext: RoutingContext) {

    }
    // ---------------- End Vendor route --------------------



    // ---------------- Product routes -------------------
    fun createProduct(routingContext: RoutingContext) {

    }

    fun updateProduct(routingContext: RoutingContext) {

    }

    fun getAllProducts(routingContext: RoutingContext) {

    }

    fun getProductById(routingContext: RoutingContext) {

    }

    fun removeProduct(routingContext: RoutingContext) {

    }

    fun removeProducts(routingContext: RoutingContext) {

    }

    fun trashProduct(routingContext: RoutingContext) {

    }

    fun trashProducts(routingContext: RoutingContext) {

    }
    // ---------------- End Product routes ---------------

    // ---------------- Combo routes --------------------
    fun createComboProduct(routingContext: RoutingContext) {

    }

    fun updateComboProduct(routingContext: RoutingContext) {

    }

    fun getAllComboProducts(routingContext: RoutingContext) {

    }

    fun getComboProductById(routingContext: RoutingContext) {

    }

    fun removeComboProduct(routingContext: RoutingContext) {

    }

    fun removeComboProducts(routingContext: RoutingContext) {

    }

    fun trashComboProduct(routingContext: RoutingContext) {

    }

    fun trashComboProducts(routingContext: RoutingContext) {

    }
    // ---------------- End Combo routes ----------------

    // ---------------- Combo routes --------------------
    fun createMatrix(routingContext: RoutingContext) {

    }

    fun updateMatrix(routingContext: RoutingContext) {

    }

    fun getAllMatrix(routingContext: RoutingContext) {

    }

    fun getMatrixById(routingContext: RoutingContext) {

    }

    fun removeMatrix(routingContext: RoutingContext) {

    }

    fun removeMatrices(routingContext: RoutingContext) {

    }

    fun trashMatrix(routingContext: RoutingContext) {

    }

    fun trashMatrices(routingContext: RoutingContext) {

    }
    // ---------------- End Combo routes ----------------

}