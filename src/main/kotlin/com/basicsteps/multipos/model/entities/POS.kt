package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class POS(@SerializedName("name") var name: String,
               @SerializedName("address") var address: String,
               @SerializedName("phone_number") var phoneNumber: String,
               @SerializedName("login") var login: String,
               @SerializedName("password") var password: String) : BaseModel() {

    constructor() : this("", "", "", "", "")

    override fun instance(): Instanceable {
        val result = POS()

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

        //specs
        result.name = name
        result.address = address
        result.phoneNumber = phoneNumber
        result.password = password

        return result
    }

}