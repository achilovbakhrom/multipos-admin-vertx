package com.basicsteps.multipos.verticles.handling.handler.config

import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.NotExistsException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.core.model.exceptions.UpdateDbFailedException
import com.basicsteps.multipos.core.model.exceptions.WriteDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class UnitHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {


    fun activateUnit(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body())
        val id = request.data!!
        dbManager
                .unitEntityDao
                ?.findById(id)
                ?.flatMap({unit ->
                    unit.active = true
                    dbManager.unitEntityDao?.updateWithoutDuplicate(unit, request.userId!!, request.companyId)
                })
                ?.subscribe({ unit ->
                    message.reply(MultiPosResponse(unit, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, { error ->
                    when (error) {
                        is ReadDbFailedException -> message.reply(MultiPosResponse<Any>(null, null, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is WriteDbFailedException -> message.reply(MultiPosResponse<Any>(null, null, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse<Any>(null, null, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is NotExistsException -> message.reply(MultiPosResponse<Any>(null, null, StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()).toJson())
                        is UpdateDbFailedException -> message.reply(MultiPosResponse<Any>(null, null, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })

    }

    fun deactivateUnit(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body())
        val id = request.data!!
        dbManager
                .unitEntityDao
                ?.findById(id)
                ?.flatMap({unit ->
                    unit.active = false
                    dbManager.unitEntityDao?.updateWithoutDuplicate(unit, request.userId!!, request.companyId)
                })
                ?.subscribe({ unit ->
                    message.reply(MultiPosResponse(unit, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, { error ->
                    when (error) {
                        is ReadDbFailedException -> message.reply(MultiPosResponse<Any>(null, null, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is WriteDbFailedException -> message.reply(MultiPosResponse<Any>(null, null, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse<Any>(null, null, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is NotExistsException -> message.reply(MultiPosResponse<Any>(null, null, StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()).toJson())
                        is UpdateDbFailedException -> message.reply(MultiPosResponse<Any>(null, null, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })

    }


    fun getUnits(message: Message<String>) {
        dbManager
                .unitEntityDao
                ?.findAll()
                ?.subscribe({units ->
                    message.reply(MultiPosResponse<Any>(units, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, {error ->
                    message.reply(MultiPosResponse<Any>(null, null, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                })
    }

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