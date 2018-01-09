package com.basicsteps.multipos.verticles

import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject

class MainVerticle : AbstractVerticle() {
    override fun start() {
        super.start()
        vertx.deployVerticle(WorkerVerticle())
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