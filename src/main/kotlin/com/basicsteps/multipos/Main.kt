package com.basicsteps.multipos

import com.basicsteps.multipos.utils.Runner
import com.basicsteps.multipos.verticles.MainVerticle
import io.vertx.core.Vertx


fun main(args: Array<String>) {
    Vertx.vertx().deployVerticle(MainVerticle())
}