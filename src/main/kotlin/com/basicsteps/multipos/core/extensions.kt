package com.basicsteps.multipos.core

import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Id
import io.vertx.core.http.HttpServerResponse

fun HttpServerResponse.putBrowserHeaders() : HttpServerResponse {
    return this.putHeader("content-type", "application/json")
            .putHeader("Access-Control-Allow-Origin", "*")
            .putHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, OPTIONS")
//            .putHeader("Access-Control-Allow-Credentials", "true")
            .putHeader("Access-Control-Allow-Headers", "Content-type,Authorization,Accept,X-Access-Token,X-Key")
}
@Entity
class Test(var id: String, var test: String)