package com.basicsteps.multipos.core.model

import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.field.Id
import de.braintags.io.vertx.pojomapper.annotation.lifecycle.BeforeSave
import java.io.Serializable
import java.util.*

abstract class BaseModel : Trashable, Activeable, Auditable, Jsonable, Instanceable, Serializable {

    @Id var id: String? = null
    @Transient
    @SerializedName("created_date")
    override var createdTime = -1L
    @Transient
    @SerializedName("modified_date")
    override var modifiedTime = -1L
    @Transient
    @SerializedName("created_by")
    override var createdBy: String? = null
    @Transient
    @SerializedName("modified_by")
    override var modifiedBy: String? = null
    @Transient
    @SerializedName("is_active")
    override var active: Boolean = false
    @Transient
    @SerializedName("is_deleted")
    override var deleted: Boolean = false
    @Transient
    @SerializedName("user_id")
    var userId: String? = null
    @Transient
    @SerializedName("root_id")
    override var rootId: String? = null
    @Transient
    @SerializedName("modified_id")
    var modifiedId: String? = null
    @Transient
    @SerializedName("companyId_id")
    override var companyId: String? = null
    @Transient
    @SerializedName("pos_id")
    override var posId: String? = null
    var access: String? = "*"


    @BeforeSave
    fun beforeSave() {
        val time = Date().time
        if (createdTime == - 1L) {
            createdTime = time
            createdBy = userId
        }
        modifiedTime = time
        modifiedBy = userId
    }
}

enum class ModelType(type: String) {
    PRIVATE("private"),
    PUBLIC("public"),
    PER_ESTABLISHMENT("per-establishment"),
    PER_COMPANY("per-company")
}