package com.basicsteps.multipos.core

import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.json.JsonObject

data class LMResponse(var status: Int, var message: String) {
    fun toJson() : String {
        return JsonUtils.toJson(this)
    }
    companion object {
        fun fromJson(json: String) : LMResponse {
            return JsonUtils.toPojo(json)
        }

        fun okMsg() : String {
            val json = JsonObject()
            json.put("msg", "OK")
            return LMResponse(HttpResponseStatus.OK.code(), json.toString()).toJson()
        }

        fun okMsg(key: String, value: String) : String {
            val json = JsonObject()
            json.put("msg", "OK")
            json.put(key, value)
            return LMResponse(HttpResponseStatus.OK.code(), json.toString()).toJson()
        }

        fun okMsg(keys: Array<String>, values: Array<String>) : String {
            val json = JsonObject()
            json.put("msg", "OK")
            for (i in 0..keys.size-1) {
                json.put(keys[i], values[i])
            }
            return LMResponse(HttpResponseStatus.OK.code(), json.toString()).toJson()
        }
    }
}