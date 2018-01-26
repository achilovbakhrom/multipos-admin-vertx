package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class Establishment(@SerializedName("name") var name: String,
                         @SerializedName("posIds") var posIds: List<String>?,
                         @SerializedName("poses") var poses: List<POS>?,
                         @SerializedName("owner_company_id") var ownerCompanyId: String) : BaseModel() {
    constructor() : this("", ArrayList(), null, "")

    override fun instance(): Instanceable {
        val result = Establishment(name, posIds, poses, ownerCompanyId)
        result.createdTime = createdTime
        result.modifiedTime = modifiedTime
        result.createdBy = createdBy
        result.modifiedBy = modifiedBy
        result.active = active
        result.deleted = deleted
        result.userId = userId
        result.rootId = rootId
        result.access = access

        //spec
        result.name = name
        result.posIds = posIds
        result.poses = poses
        result.ownerCompanyId = ownerCompanyId
        return result
    }
}