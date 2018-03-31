package com.basicsteps.multipos.core.response

import com.basicsteps.multipos.utils.JsonUtils

class MultiposRequest<T> {
    var data: T? = null
    var userId: String? = null
    var companyId: String? = null

    fun toJson(): String {
        return JsonUtils.toJson(this)
    }
}