package com.basicsteps.multipos.core

import com.basicsteps.multipos.utils.JsonUtils
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.lifecycle.BeforeSave
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import java.util.*

abstract class BaseModel {
    @SerializedName("created_date")
    private var createdDate: Date? = null
    @SerializedName("modified_date")
    private var modifiedDate: Date? = null
    @SerializedName("created_by")
    private var createdBy: String? = null
    @SerializedName("modified_by")
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