package com.basicsteps.multipos.model

import com.basicsteps.multipos.core.model.BaseModel
import com.google.gson.annotations.SerializedName

data class Company(@SerializedName("id") var id: Long,
                   @SerializedName("name") var name: String,
                   @SerializedName("zip_code") var zipCode: Int,
                   @SerializedName("phone") var phone: String,
                   @SerializedName("contacts") var contacts: List<Contact>) //: BaseModel()