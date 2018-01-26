package com.basicsteps.multipos.verticles.handling.handler.config

import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.entities.Establishment
import com.basicsteps.multipos.model.entities.POS
import com.basicsteps.multipos.model.entities.Stock
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message

class POSHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {

    fun createPOS(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<POS>>(message.body().toString())
        dbManager
                .posDao
                ?.save(request.data!!, request.userId!!, request.companyId!!)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                }, {error ->

                })
    }

    fun trashPOS(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<POS>>(message.body().toString())
        dbManager
                .posDao
                ?.trash(request.data!!, request.userId!!, request.companyId!!)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                }, {error ->

                })
    }

    fun updatePOS(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<POS>>(message.body().toString())
        dbManager
                .posDao
                ?.update(request.data!!, request.userId!!, request.companyId!!)
                ?.subscribe({result ->
                    message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                }, {error ->

                })
    }

    fun getPOSesByCompanyId(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<POS>>(message.body().toString())
        val companyId = request.data
        dbManager
                .posDao
                ?.findAll()
                ?.subscribe({result ->
                    val res = mutableListOf<POS>()
                    for (pos in result) {
                        if (pos.owner_company_id.equals(companyId)) {
                            result.add(pos)
                        }
                    }
                    message.reply(MultiPosResponse<List<POS>>(res, null, "OK", HttpResponseStatus.OK.code()))
                }, {error ->

                })

    }

    fun createEstablishment(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Establishment>>(message.body().toString())
        dbManager
                .establishmentDao
                ?.save(request.data!!, request.userId!!, request.companyId!!)
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                }, {error ->

                })
    }

    fun trashEstablishment(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Establishment>>(message.body().toString())
        dbManager
                .establishmentDao
                ?.trash(request.data!!, request.userId!!, request.companyId!!)
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                }, {error ->

                })
    }

    fun updateEstablishment(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Establishment>>(message.body().toString())
        dbManager
                .establishmentDao
                ?.update(request.data!!, request.userId!!, request.companyId!!)
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                }, {error ->

                })
    }

    fun getEstablishmentByCompanyId(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
        val companyId = request.data
        dbManager
                .establishmentDao
                ?.findAll()
                ?.subscribe({result ->
                    val res = mutableListOf<Establishment>()
                    for (estalblishment in result) {
                        if (estalblishment.ownerCompanyId.equals(companyId)) {
                            res.add(estalblishment)
                        }
                    }
                    message.reply(MultiPosResponse<List<Establishment>>(res, null, "OK", HttpResponseStatus.OK.code()))
                }, {error ->

                })
    }

    fun createStock(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Stock>>(message.body().toString())
        dbManager
                .stockDao
                ?.save(request.data!!, request.userId!!, request.companyId!!)
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                }, {error ->

                })

    }

    fun trashStock(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Stock>>(message.body().toString())
        dbManager
                .stockDao
                ?.trash(request.data!!, request.userId!!, request.companyId!!)
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                }, {error ->

                })
    }

    fun updateStock(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<Stock>>(message.body().toString())
        dbManager
                .stockDao
                ?.update(request.data!!, request.userId!!, request.companyId!!)
                ?.subscribe({ result ->
                    message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                }, {error ->

                })
    }

    fun getStocksByPOSId(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
        val posId = request.data
        dbManager
                .stockDao
                ?.findAll()
                ?.subscribe({result ->
                    val res = mutableListOf<Stock>()
                    for (stock in result) {
                        if (stock.accessPosId?.contains(posId)) {
                            res.add(stock)
                        }
                    }
                    message.reply(MultiPosResponse<List<Stock>>(res, null, "OK", HttpResponseStatus.OK.code()))
                }, { error ->

                })
    }

    fun getStocksByEstablishmentId(message: Message<String>) {
        val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
        val establishmentId = request.data
        dbManager
                .stockDao
                ?.findAll()
                ?.subscribe({result ->
                    val res = mutableListOf<Stock>()
                    for (stock in result) {
                        if (stock.accessEstablishmentId?.contains(establishmentId)) {
                            res.add(stock)
                        }
                    }
                    message.reply(MultiPosResponse<List<Stock>>(res, null, "OK", HttpResponseStatus.OK.code()))
                }, { error ->

                })
    }

}