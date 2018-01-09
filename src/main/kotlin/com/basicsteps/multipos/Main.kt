package com.basicsteps.multipos

import com.basicsteps.multipos.utils.Runner
import com.basicsteps.multipos.verticles.MainVerticle
import io.vertx.core.Vertx


fun main(args: Array<String>) {

    Vertx.vertx().deployVerticle(MainVerticle())

//    val vertx = Vertx.vertx()
//    val js = JsonObject()
//    val config = JsonConfigurer.getConfig()
//    config.setHandler({ res ->
//        if (res.succeeded()) {
//            print("successful")
//        } else {
//            print(res.cause())
//        }
//
//    })
//    OpenAPI3RouterFactory.createRouterFactoryFromFile(vertx, "src/main/resources/api/api.yaml") { openAPI3RouterFactoryAsyncResult ->
//        if (openAPI3RouterFactoryAsyncResult.succeeded()) {
//            // Spec loaded with success
//            val routerFactory = openAPI3RouterFactoryAsyncResult.result()
//
//            // Add an handler with operationId
//            routerFactory.addHandlerByOperationId("helloWorld", { routingContext ->
//                // Handle listPets operation
//                routingContext.response().end("Hello World! PRivet mir")
//            })
//
//            // Add an handler with a combination of HttpMethod and path
//            // Before router creation you can enable or disable mounting a default failure handler for ValidationException
//            routerFactory.enableValidationFailureHandler(false)
//            // Now you have to generate the router
//            val router = routerFactory.router
//            // Now you can use your Router instance
//            val server = vertx.createHttpServer(HttpServerOptions().setPort(8081).setHost("localhost"))
//            server.requestHandler(router::accept).listen()
//        } else {
//            // Something went wrong during router factory initialization
//            val exception = openAPI3RouterFactoryAsyncResult.cause()
//            print("Ops! $exception")
//        }
//    }
//
//    vertx.deployVerticle(SignUpVerticle())

//    vertx.createHttpServer().requestHandler({ req ->
//        run {
//            req.response().end("privet");
//        }
//    }).listen(8081)
//    var router = OpenAPI3RouterFactory.createRouterFactoryFromFile(vertx, "", {
//        handler ->
//        run {
//            var test = handler.result();
//
//        }
//    })

}