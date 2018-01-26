package com.basicsteps.multipos.config

import com.basicsteps.multipos.model.entities.Currency
import com.basicsteps.multipos.model.entities.UnitCategoryEntity
import com.basicsteps.multipos.utils.ConfigUtils
import io.vertx.core.Vertx


object Resources {
    val vertx = Vertx.vertx()

    var unitCategories: List<UnitCategoryEntity>? = null
        get() {
            if (field == null) {
                field = ConfigUtils.getUnitCategories(vertx).blockingFirst()
            }
            return field
        }

    var currencies: List<Currency>? = null
        get() {
            if (field == null) {
                currencies = ConfigUtils.getCurrencies(vertx).blockingFirst()
            }
            return field
        }
}