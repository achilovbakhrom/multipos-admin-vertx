package com.basicsteps.multipos.verticles

import com.basicsteps.multipos.managers.db.DbManager
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject

class MainVerticle : AbstractVerticle() {
    var dbManager: DbManager? = null
    override fun start() {
        super.start()
        dbManager = DbManager(vertx)
        vertx.deployVerticle(SignUpHandler(dbManager!!))
        vertx.deployVerticle(SignInHandler(dbManager!!))
        vertx.deployVerticle(OpenApiRoutingVerticle())
        val js = JsonObject()
        vertx.fileSystem().readFile("src/main/resources/config.json") { result ->
            if (result.succeeded()) {
                val buff = result.result()
                js.mergeIn(JsonObject(buff.toString()))

            } else {
                System.err.println("Oh oh ... " + result.cause())
            }
        }
    }


    override fun stop() {
        super.stop()

    }
}