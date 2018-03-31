package com.basicsteps.multipos.verticles.handling.handler.config

import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.entities.Currency
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message

class CurrencyHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {

    fun getCurrencies(message: Message<String>) {
        dbManager
                .currencyDao
                ?.findAll()
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<List<Currency>>(result, null, StatusMessages.SUCCESS.status, HttpResponseStatus.OK.code()).toJson())
                }, { error ->

                })
    }

    fun updateCurrency(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Currency>>(message.body().toString())
        dbManager
                .currencyDao
                ?.update(request.data!!, request.userId!!, request.companyId)
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<Any>(null, null, StatusMessages.SUCCESS.status, HttpResponseStatus.OK.code()).toJson())
                }, {error ->

                })
    }

    fun trashCurrency(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Currency>>(message.body().toString())
        dbManager
                .currencyDao
                ?.trash(request.data!!, request.userId!!, request.companyId)
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<Any>(null, null, StatusMessages.SUCCESS.status, HttpResponseStatus.OK.code()).toJson())
                }, {error ->

                })
    }

}