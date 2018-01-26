package com.basicsteps.multipos.verticles.handling.handler.config

import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message


class ConfigHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {
    //handlers
    var unitCategoryHandler: UnitCategoryHandler? = null
    var unitHandler: UnitHandler? = null
    var currencyHandler: CurrencyHandler? = null
    var accountHandler: AccountHandler? = null
    var paymentTypeHandler: PaymentTypeHandler? = null
    var vendorHandler: VendorHandler? = null
    var productHandler: ProductHandler? = null
    var categoryHandler: CategoryHandler? = null
    var subCategoryHandler: SubCategoryHandler? = null
    var companyhandler: CompanyHandler? = null

    init {
        unitHandler = UnitHandler(vertx, dbManager)
        unitCategoryHandler = UnitCategoryHandler(vertx, dbManager)
        currencyHandler = CurrencyHandler(vertx, dbManager)
        accountHandler = AccountHandler(vertx, dbManager)
        paymentTypeHandler = PaymentTypeHandler(vertx, dbManager)
        vendorHandler = VendorHandler(vertx, dbManager)
        productHandler = ProductHandler(vertx, dbManager)
        categoryHandler = CategoryHandler(vertx, dbManager)
        subCategoryHandler = SubCategoryHandler(vertx, dbManager)
        companyhandler = CompanyHandler(vertx, dbManager)

    }

    //company
    fun creatCompany(message: Message<String>) { companyhandler?.createCompany(message) }
    fun addUserToCompany(message: Message<String>) { companyhandler?.addUserToCompany(message) }
    fun trashCompany(message: Message<String>) { companyhandler?.trashCompany(message) }
    fun updateCompany(message: Message<String>) { companyhandler?.updateCompany(message) }
    fun getComaniesByUserId(message: Message<String>) { companyhandler?.getComaniesByUserId(message) }

    //establishment vs pos



    //units
    fun getUnitCategories(message: Message<String>) { unitCategoryHandler?.getUnitCategories(message) }
    fun getUnitCategoriesWithUnits(message: Message<String>) { unitCategoryHandler?.getUnitCategoriesWithUnits(message) }
    fun updateUnit(message: Message<String>) {  }


    //category
    fun getCateries(message: Message<String>) { categoryHandler?.getCategories(message) }

    //subcategory
    fun getSubcategories(message: Message<String>) { subCategoryHandler?.getSubcategories(message) }

    //currency
    fun getCurrencies(message: Message<String>) { currencyHandler?.getCurrencies(message) }

    //account
    fun getAccounts(message: Message<String>) {accountHandler?.getAccounts(message)}

    //Payment type
    fun getPaymentTypes(message: Message<String>) { paymentTypeHandler?.getPaymentTypes(message) }

    //Vendor
    fun getVendors(message: Message<String>) { vendorHandler?.getVendors(message) }

    //products
    fun getProducts(message: Message<String>) { productHandler?.getProducts(message) }

    //combo
    fun getCombos(message: Message<String>) { TODO("get combos") }

    //matrix
    fun getMatrices(message: Message<String>) { TODO("get matrices") }
}
