package com.basicsteps.multipos.model

import com.basicsteps.multipos.core.BaseModel

data class Company(var id: Long, var name: String, var zipCode: Int, var phone: String, var contacts: List<Contact>) : BaseModel()