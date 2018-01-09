package com.basicsteps.multipos.verticles

import com.basicsteps.multipos.config.EndpointUriTypes
import com.basicsteps.multipos.routers.SignUpRouter
import io.vertx.core.AbstractVerticle
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory

class OpenApiRoutingVerticle() : AbstractVerticle() {
    var server: HttpServer? = null
    var signUpRouter: SignUpRouter? = null

    override fun start() {
        super.start()
        signUpRouter = SignUpRouter(vertx)
        server = vertx.createHttpServer()
        OpenAPI3RouterFactory.createRouterFactoryFromFile(vertx, "src/main/resources/api.yaml") { openAPI3RouterFactoryAsyncResult ->
            if (openAPI3RouterFactoryAsyncResult.succeeded()) {
                // Spec loaded with success
                val routerFactory = openAPI3RouterFactoryAsyncResult.result()

                // Add an handler with operationId
//                routerFactory.addHandlerByOperationId("helloWorld", { routingContext ->
//                    routingContext.response().end("Hello World! PRivet mir")
//                })

                routerFactory.addHandlerByOperationId(EndpointUriTypes.SIGN_UP.endpoint, { routingContext -> signUpRouter?.signUp(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriTypes.SIGN_IN.endpoint, { routingContext -> signUpRouter?.signIn(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriTypes.CONFIRM_ACCESS_CODE.endpoint, { routingContext -> signUpRouter?.confirmAccessCode(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriTypes.IS_EMAIL_EXISTS.endpoint, { routingContext -> signUpRouter?.isEmailExists(routingContext) })


                // Add an handler with a combination of HttpMethod and path
                // Before router creation you can enable or disable mounting a default failure handler for ValidationException
                routerFactory.enableValidationFailureHandler(false)
                // Now you have to generate the router
                val router = routerFactory.router
                // Now you can use your Router instance
                val server = vertx.createHttpServer(HttpServerOptions().setPort(8081).setHost("localhost"))
                server.requestHandler(router::accept).listen()
                print("server is run\n")
            } else {
                // Something went wrong during router factory initialization
                val exception = openAPI3RouterFactoryAsyncResult.cause()
                print("OpenApiRoutingError! $exception")
            }
        }

    }

    override fun stop() {
        super.stop()
        server?.close()
    }
}