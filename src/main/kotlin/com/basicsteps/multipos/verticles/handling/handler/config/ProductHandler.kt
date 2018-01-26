package com.basicsteps.multipos.verticles.handling.handler.config

import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.response.MultiPosResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message

class ProductHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {

    fun getProducts(message: Message<String>) {
        dbManager
                .productDao
                ?.findAll()
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<Any>(result, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, { error ->

                })
    }

}