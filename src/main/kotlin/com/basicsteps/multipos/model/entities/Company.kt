package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class Company(@SerializedName("name") var name: String,
                   @SerializedName("address") var address: String,
                   @SerializedName("phone_number") var phoneNumber: String,
                   @SerializedName("zip_code") var zipCode: Int,
                   @SerializedName("owners") var ownersId: MutableList<String>,
                   @SerializedName("type") var type: String) : BaseModel() {

    constructor() : this("", "", "", 0, mutableListOf(), "")

    override fun instance(): Instanceable {
        val result = Company(name, address, phoneNumber, zipCode, ownersId, type)
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
        result.address = address
        result.phoneNumber = phoneNumber
        result.zipCode = zipCode
        result.ownersId = ownersId
        result.type = type
        return result
    }
}


enum class CompanyType(val type: String) { COMPANY("COMPANY"), PROVIDER("PROVIDER"), COMPANY_PROVIDER("COMPANY_PROVIDER") }