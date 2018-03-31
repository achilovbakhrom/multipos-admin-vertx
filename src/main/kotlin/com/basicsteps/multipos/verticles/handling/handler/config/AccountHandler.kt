package com.basicsteps.multipos.verticles.handling.handler.config

import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.entities.Account
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message

class AccountHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {

    fun getAccounts(message: Message<String>) {
        dbManager
                .accountDao
                ?.findAll()
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<Any>(result, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, { error ->

                })
    }

    fun createAccount(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Account>>(message.body().toString())
        dbManager
                .accountDao
                ?.save(request.data!!, request.userId!!, request.companyId)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, {error ->

                })
    }

    fun udpdateAccount(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Account>>(message.body().toString())

        dbManager
                .accountDao
                ?.update(request.data!!, request.userId!!, request.companyId)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, {error ->

                })
    }

    fun trashAccount(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Account>>(message.body().toString())
        dbManager
                .accountDao
                ?.trash(request.data!!, request.userId!!, request.companyId)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, {error ->

                })
    }

}