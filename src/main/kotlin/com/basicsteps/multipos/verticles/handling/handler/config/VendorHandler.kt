package com.basicsteps.multipos.verticles.handling.handler.config

import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.entities.Account
import com.basicsteps.multipos.model.entities.Vendor
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message

class VendorHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {

    fun getVendors(message: Message<String>) {
        dbManager
                .vendorDao
                ?.findAll()
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<Any>(result, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, { error ->

                })
    }

    fun createAccount(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Vendor>>(message.body().toString())
        dbManager
                .vendorDao
                ?.save(request.data!!, request.userId!!, request.companyId)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, {error ->

                })
    }

    fun updateAccount(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Vendor>>(message.body().toString())
        dbManager
                .vendorDao
                ?.update(request.data!!, request.userId!!, request.companyId)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, {error ->

                })
    }

    fun trashAccount(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Vendor>>(message.body().toString())
        dbManager
                .vendorDao
                ?.trash(request.data!!, request.userId!!, request.companyId)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, {error ->

                })
    }
}