package com.basicsteps.multipos.model.sign_up

import com.basicsteps.multipos.core.BaseModel
import com.basicsteps.multipos.model.Contact
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded
import de.braintags.io.vertx.pojomapper.annotation.field.Id
import de.braintags.io.vertx.pojomapper.annotation.field.Property


@Entity
data class SignUpMapper(@Id var id: String,
                        @SerializedName("mail") var mail: String,
                        @SerializedName("password") var password: String,
                        @SerializedName("first_name") var firstName: String,
                        @SerializedName("last_name") var lastName: String,
                        @SerializedName("middle_name") var middleName: String,
                        @SerializedName("birthday") var birthday: String,
                        @SerializedName("sex") var sex: String,
                        @SerializedName("avatar_url") var avatarUrl: String,
                        @SerializedName("primary_phone") var primaryPhone: String,
                        @SerializedName("contacts") @Embedded var contacts: List<Contact>,
                        @SerializedName("country") var country: String,
                        @SerializedName("access_code") var accessCode: Int) : BaseModel() {
    constructor() : this("", "", "", "", "", "", "", "", "", "",
            ArrayList<Contact>(), "", -1)
}