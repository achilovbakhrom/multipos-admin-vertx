package com.basicsteps.multipos.model

import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Id

@Entity
data class Contact(@Id var id: String, var type: ContactType, var data: String) {
    constructor() : this("", ContactType.EMAIL, "")
}