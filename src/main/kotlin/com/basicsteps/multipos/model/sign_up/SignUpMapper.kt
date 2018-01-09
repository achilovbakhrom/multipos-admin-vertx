package com.basicsteps.multipos.model.sign_up

import com.basicsteps.multipos.core.BaseModel
import com.basicsteps.multipos.model.Contact
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded
import de.braintags.io.vertx.pojomapper.annotation.field.Id
import de.braintags.io.vertx.pojomapper.annotation.field.Property


@Entity
data class SignUpMapper(@Id var id: String,
                        var mail: String,
                        var password: String,
                        var firstName: String,
                        var lastName: String,
                        var middleName: String,
                        var birthday: String,
                        var sex: String,
                        var avatarUrl: String,
                        var primaryPhone: String,
                        @Embedded var contacts: List<Contact>,
                        var country: String,
                        var accessCode: Int) : BaseModel() {
    constructor() : this("", "", "", "", "", "", "", "", "", "",
            ArrayList<Contact>(), "", -1)
}