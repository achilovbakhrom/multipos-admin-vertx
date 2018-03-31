package com.basicsteps.multipos.verticles.handling.handler.config

import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.entities.Product
import com.basicsteps.multipos.utils.JsonUtils
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

    fun createProduct(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Product>>(message.body().toString())
        dbManager
                .productDao
                ?.save(request.data!!, request.userId!!, request.companyId)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, {error -> })
    }

    fun updateProduct(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Product>>(message.body().toString())
        dbManager
                .productDao
                ?.update(request.data!!, request.userId!!, request.companyId)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, {error -> })
    }

    fun trashProduct(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Product>>(message.body().toString())
        dbManager
                .productDao
                ?.trash(request.data!!, request.userId!!, request.companyId)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, {error ->

                })
    }
}