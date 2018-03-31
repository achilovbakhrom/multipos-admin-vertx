package com.basicsteps.multipos.utils

import com.basicsteps.multipos.model.Config
import io.vertx.core.Future

import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject

object JsonConfigurer {

    private var future: Future<Config> = Future.future()

    init {
        val js = JsonObject()
        Vertx.vertx().fileSystem().readFile("src/main/resources/entities.json") { result ->
            if (result.succeeded()) {
                val buff = result.result()
                js.mergeIn(JsonObject(buff.toString()))
                val config = Config(
                        js.getString("host"),
                        js.getInteger("port"),
                        js.getString("mongoUri"),
                        js.getString("mongoSignUpDbName"),
                        js.getString("mongoSignInDbName"),
                        js.getString("keycloakAdmin"),
                        js.getString("keycloakAdminPassword"))
                future.complete(config)
            } else {
                System.err.println("Reading configuration file is failed: " + result.cause())
            }
        }
    }

    fun getConfig(): Future<Config> {
        return future
    }

}