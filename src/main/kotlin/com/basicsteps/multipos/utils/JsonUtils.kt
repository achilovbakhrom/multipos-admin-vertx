package com.basicsteps.multipos.utils

import com.google.gson.Gson
import io.vertx.core.json.JsonObject

object JsonUtils {

    inline fun <reified T> toPojo(json: String): T {
        return Gson().fromJson(json, T::class.java)
    }

    fun <T> toJson(t: T): String {
        return Gson().toJson(t)
    }

    fun <T> toJsonObject(pojo: T) : JsonObject {
        return JsonObject(toJson(pojo))
    }

}