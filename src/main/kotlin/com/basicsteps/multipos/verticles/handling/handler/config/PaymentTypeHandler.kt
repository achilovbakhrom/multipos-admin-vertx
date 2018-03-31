package com.basicsteps.multipos.verticles.handling.handler.config

import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.entities.PaymentType
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message

class PaymentTypeHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {

    fun getPaymentTypes(message: Message<String>) {
        dbManager
                .paymentTypeDao
                ?.findAll()
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<Any>(result, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, { error ->

                })
    }

    fun createPaymentType(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<PaymentType>>(message.body().toString())
        dbManager
                .paymentTypeDao
                ?.save(request.data!!, request.userId!!, request.companyId)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, { error ->

                })
    }

    fun updatePaymentType(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<PaymentType>>(message.body().toString())
        dbManager
                .paymentTypeDao
                ?.update(request.data!!, request.userId!!, request.companyId)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, { error ->

                })
    }

    fun trashPaymentType(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<PaymentType>>(message.body().toString())
        dbManager
                .paymentTypeDao
                ?.trash(request.data!!, request.userId!!, request.companyId)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "Success", HttpResponseStatus.OK.code()).toJson())
                }, { error ->

                })
    }
}