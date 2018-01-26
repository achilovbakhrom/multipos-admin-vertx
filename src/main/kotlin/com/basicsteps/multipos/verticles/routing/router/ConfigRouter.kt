package com.basicsteps.multipos.verticles.routing.router

import com.basicsteps.multipos.event_bus_channels.ConfigHandlerChannel
import com.basicsteps.multipos.utils.RoutingUtils
import io.vertx.core.Vertx
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

    // ---------- Unit Category routes ---------
    fun getUnitCategories(routingContext: RoutingContext) { RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.GET_UNIT_CATEGORY.value()) }
    fun getUnitCategoriesWithUnits(routingContext: RoutingContext) { RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.GET_UNIT_CATEGORY_WITH_UNITS.value()) }
    fun activateUnits(routingContext: RoutingContext) { RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.GET_UNIT_CATEGORY_WITH_UNITS.value()) }
    fun activateUnit(routingContext: RoutingContext) { RoutingUtils.route(vertx, routingContext, ConfigHandlerChannel.GET_UNIT_CATEGORY_WITH_UNITS.value()) }
    // ------------- End unit routes -------------

    // ------------- Currency routes --------------
    fun getCurrencies(routingContext: RoutingContext) {

    }

    fun cactrivateCurrencies(routingContext: RoutingContext) {

    }

    fun activateCurrency(routingContext: RoutingContext) {

    }
    // -------------- End currency routes -----------

    // -------------- Account routes ----------------
    fun createAccount(routingContext: RoutingContext) {

    }

    fun updateAccount(routingContext: RoutingContext) {

    }

    fun getAccounts(routingContext: RoutingContext) {

    }

    fun getAccountById(routingContext: RoutingContext) {

    }

    fun removeAccounts(routingAcontext: RoutingContext) {

    }

    fun removeAccount(routingContext: RoutingContext) {

    }

    fun trashAccount(routingContext: RoutingContext) {

    }

    fun trashAccounts(routingContext: RoutingContext) {

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