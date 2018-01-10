package com.basicsteps.multipos.core

import io.vertx.core.http.HttpServerResponse

fun HttpServerResponse.putBrowserHeaders() : HttpServerResponse {
    return this.putHeader("content-type", "application/json")
            .putHeader("Access-Control-Allow-Origin", "*")
            .putHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, OPTIONS")
//            .putHeader("Access-Control-Allow-Credentials", "true")
            .putHeader("Access-Control-Allow-Headers", "Content-type,Authorization,Accept,X-Access-Token,X-Key")
}