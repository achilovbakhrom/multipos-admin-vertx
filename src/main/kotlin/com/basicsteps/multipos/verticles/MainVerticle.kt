package com.basicsteps.multipos.verticles

import com.basicsteps.multipos.verticles.handling.WorkerVerticle
import com.basicsteps.multipos.verticles.routing.OpenApiRoutingVerticle
import io.vertx.core.AbstractVerticle


class MainVerticle : AbstractVerticle() {

    override fun start() {
        super.start()
        vertx.deployVerticle(WorkerVerticle())
        vertx.deployVerticle(OpenApiRoutingVerticle())
    }
}