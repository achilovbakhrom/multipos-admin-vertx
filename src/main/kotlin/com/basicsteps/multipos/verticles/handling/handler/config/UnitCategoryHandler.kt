package com.basicsteps.multipos.verticles.handling.handler.config

import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.entities.UnitCategoryEntity
import com.basicsteps.multipos.verticles.handling.dao.UnitCategoryEntityDao
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.Observable
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

class UnitCategoryHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {

    fun getUnitCategories(message: Message<String>) {
        dbManager.unitCategoryEntityDao
                ?.findAll()
                ?.subscribe({list ->
                    if (list != null && !list.isEmpty()) {
                        message.reply(MultiPosResponse<List<UnitCategoryEntity>>(list, null, "Success", HttpResponseStatus.OK.code()).toJson())
                    } else {
                        message.reply(MultiPosResponse<Any>(null, "list is empty", "Error", HttpResponseStatus.NOT_FOUND.code()).toJson())
                    }
                }, {error ->
                    when(error) {
                        is ReadDbFailedException -> message.reply(MultiPosResponse<Any>(null, error.message, "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse<Any>(null, error.message, "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    fun getUnitCategoriesWithUnits(message: Message<String>) {
        print("test")
        dbManager.unitCategoryEntityDao
                ?.findAll()
                ?.subscribe({list ->
                    if (list != null && !list.isEmpty()) {
                        val result = JsonArray()
                        var temp: JsonObject? = null
                        var count = 0
                        val test = Observable
                                .fromArray(list)
                                .flatMapIterable({iter -> iter})
                                .flatMap({item ->
                                    temp = JsonObject(item.toJson())
                                    dbManager.unitEntityDao?.getUnitsByUnitCategoryId(item.id!!)
                                })
                                .doOnDispose({
                                    message.reply(MultiPosResponse(result, null, "Success", HttpResponseStatus.OK.code()).toJson())
                                })
                                .subscribe({ units ->
                                    temp?.put("units", units)
                                    result.add(temp)
                                    if (count == list.size - 1) {
                                        message.reply(MultiPosResponse(result, null, "Success", HttpResponseStatus.OK.code()).toJson())
                                    } else
                                        count++
                                }, {error ->
                                    error.printStackTrace()
                                })
                    } else {
                        message.reply(MultiPosResponse<Any>(null, "${UnitCategoryEntityDao::clazz.name} list is empty", "Error", HttpResponseStatus.NOT_FOUND.code()).toJson())
                    }
                }, {error ->
                    when(error) {
                        is ReadDbFailedException -> message.reply(MultiPosResponse<Any>(null, error.message, "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse<Any>(null, error.message, "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }
}