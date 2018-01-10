package com.basicsteps.multipos.model

import com.basicsteps.multipos.core.BaseModel
import com.google.gson.annotations.SerializedName
import java.util.*


data class User(@SerializedName("login") var login: String,
                @SerializedName("password") var password: String,
                @SerializedName("first_name") var firstName: String,
                @SerializedName("last_name") var lastName: String,
                @SerializedName("middle_name") var middleName: String,
                @SerializedName("sex") var sex: String,
                @SerializedName("birth_data") var birthData: Date,
                @SerializedName("primary_phone") var primaryPhone: String,
                @SerializedName("primary_email") var primaryEmail: String,
                @SerializedName("country") var country: String,
                @SerializedName("contacts") var contacts: List<Contact>,
                @SerializedName("company") var company: Company) : BaseModel()