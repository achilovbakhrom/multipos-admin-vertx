package com.basicsteps.multipos.verticles.handling.handler.config

import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class UnitHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {

    fun setUnitActiveness(message: Message<String>) {
        val jsonObject = JsonObject(message.body().toString())
        val unitId = jsonObject.getString("unit_id")
        val isActive = jsonObject.getBoolean("active")
        val userId = jsonObject.getString("user_id")
        dbManager
                .unitEntityDao
                ?.findById(unitId)
                ?.flatMap({ unit ->
                    unit.active = isActive
                    dbManager.unitEntityDao?.save(unit, userId)
                })
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<Any>("", "", "OK", HttpResponseStatus.OK.code()))
                }, { error ->
                    when (error) {
                        is ReadDbFailedException -> print(error.message)
                    }
                })
    }

}