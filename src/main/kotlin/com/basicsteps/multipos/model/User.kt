package com.basicsteps.multipos.model

import com.basicsteps.multipos.core.BaseModel
import java.util.*


data class User(var login: String,
                var password: String,
                var firstName: String,
                var lastName: String,
                var middleName: String,
                var sex: String,
                var birthData: Date,
                var primaryPhone: String,
                var primaryEmail: String,
                var country: String,
                var contacts: List<Contact>,
                var company: Company) : BaseModel()