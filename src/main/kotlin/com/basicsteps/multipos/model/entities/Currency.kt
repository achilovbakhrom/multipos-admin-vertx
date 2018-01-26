package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class Currency(@SerializedName("name") var name: String,
                    @SerializedName("abbr") var abbr: String,
                    @SerializedName("main") var main: Boolean) : BaseModel() {
    constructor() : this("", "", false)

    override fun instance() : Instanceable {
        val result = Currency(name, abbr, main)
        //base
        result.createdTime = createdTime
        result.modifiedTime = modifiedTime
        result.createdBy = createdBy
        result.modifiedBy = modifiedBy
        result.active = active
        result.deleted = deleted
        result.userId = userId
        result.rootId = rootId
        result.modifiedId = modifiedId
        result.companyId = companyId
        result.posId = posId
        result.access = access

        //spec
        result.name = name
        result.abbr = abbr
        result.main = main

        return result
    }
}