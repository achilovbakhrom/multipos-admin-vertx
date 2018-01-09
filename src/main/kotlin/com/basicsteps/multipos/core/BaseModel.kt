package com.basicsteps.multipos.core

import com.basicsteps.multipos.utils.JsonUtils
import de.braintags.io.vertx.pojomapper.annotation.lifecycle.BeforeSave
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import java.util.*

abstract class BaseModel {

    private var createdDate: Date? = null
    private var modifiedDate: Date? = null
    private var createdBy: String? = null
    private var modifiedBy: String? = null

    fun toJson() = JsonUtils.toJson(this)

    @BeforeSave
    fun beforeSave() {
        if (createdDate == null) {
            createdDate = Date()
        }
        modifiedDate = Date()


    }




}