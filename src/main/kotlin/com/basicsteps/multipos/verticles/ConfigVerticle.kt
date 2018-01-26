package com.basicsteps.multipos.verticles

import com.basicsteps.multipos.core.AbstractDbVerticle
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.event_bus_channels.ConfigHandlerChannel
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.model.entities.UnitCategoryEntity
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.eventbus.Message


class ConfigVerticle(dbManager: DbManager) : AbstractDbVerticle(dbManager) {

    override fun start() {
        super.start()
        initConsumers()
    }

    private fun initConsumers() {
        vertx.eventBus().consumer<String>(ConfigHandlerChannel.GET_UNIT_CATEGORY.value(), { message: Message<String>? -> this.getUnitCategories(message) })
    }

    fun getUnitCategories(message: Message<String>?) {
        dbManager.unitCategoryEntityDao?.findAll()?.subscribe({result ->
            message?.reply(MultiPosResponse<List<UnitCategoryEntity>>(result, null, "Success", HttpResponseStatus.OK.code()).toJson())
        }, {error ->
            message?.reply(MultiPosResponse<Any>(null, "Error while loading data from db!!!", "Error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
        })
    }

}