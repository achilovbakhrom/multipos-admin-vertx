package com.basicsteps.multipos.model.sign_up

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity


@Entity
data class SignUpMapper(@SerializedName("mail") var mail: String,
                        @SerializedName("password") var password: String,
                        @SerializedName("image_url") var imageUrl: String?,
                        @SerializedName("first_name") var firstName: String?,
                        @SerializedName("last_name") var lastName: String?,
                        @SerializedName("birthday") var birthday: String?,
                        @SerializedName("sex") var sex: String?,
                        @SerializedName("primary_phone") var primaryPhone: String?,
                        @SerializedName("country") var country: String?,
                        @SerializedName("access_code") var accessCode: Int) : BaseModel() {

    override fun instance(): Instanceable {

        val result = SignUpMapper()
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

        //specific
        result.mail = mail
        result.password = password
        result.firstName = firstName
        result.lastName = lastName
        result.birthday = birthday
        result.sex = sex
        result.primaryPhone = primaryPhone
        result.country = country
        result.accessCode = accessCode
        return result
    }

    constructor() : this("", "","", "", "", "",  "", "",
            "", -1)
}