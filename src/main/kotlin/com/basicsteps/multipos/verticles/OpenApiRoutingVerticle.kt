package com.basicsteps.multipos.verticles

import com.basicsteps.multipos.config.EndpointUriOperationId
import com.basicsteps.multipos.routers.SignInRouter
import com.basicsteps.multipos.routers.SignUpRouter
import com.basicsteps.multipos.utils.vertx_bug_fixed.OAuth2AuthHandlerImpl
import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.oauth2.OAuth2FlowType
import io.vertx.ext.auth.oauth2.providers.KeycloakAuth
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory
import io.vertx.ext.web.handler.CorsHandler

class OpenApiRoutingVerticle : AbstractVerticle() {
    var server: HttpServer? = null
    var signUpRouter: SignUpRouter? = null
    var signInRouter: SignInRouter? = null
    override fun start() {
        super.start()
        signUpRouter = SignUpRouter(vertx)
        signInRouter = SignInRouter(vertx)
        server = vertx.createHttpServer()
        OpenAPI3RouterFactory.createRouterFactoryFromFile(vertx, "src/main/resources/api.yaml") { openAPI3RouterFactoryAsyncResult ->
            if (openAPI3RouterFactoryAsyncResult.succeeded()) {
                // Spec loaded with success
                val routerFactory = openAPI3RouterFactoryAsyncResult.result()

                // Add an handler with operationId
//                routerFactory.addHandlerByOperationId("helloWorld", { routingContext ->
//                    routingContext.response().end("Hello World! PRivet mir")
//                })

                val keycloakJson = JsonObject("{\n" +
                        "\"realm\": \"master\",\n" +
                        "\"bearer-only\": true,\n" +
                        "\"auth-server-url\": \"http://localhost:8080/auth\",\n" +
                        "\"ssl-required\": \"external\",\n" +
                        "\"realm-public-key\": \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn9Xya697ZVZzQidld4uCwRoWmLyWBDQQhn+EL1e0WDUWq9v39OBpM+HadkYlOMvfU1A8ohGZZVBkKV4w35gkm3bFPluCPsWxdcqD1NNF6BnIC6bRicgP/4beeehff8nWI3mFAfH7Q7Ik8mm8BDQYhOPRx50JBkDiIQ7AlAjNJ+5/eIj6Pt/eZSmMSk+vM4Xu64E0mCZfHpN+VPQejNBz7h9nEdi3swIIo0ot2+5PZGELX/2Dek7cY4RMKGb+rvU6ug3UvZHQ985KuubKsWMCs8A80yWSoA6umw1DC5rAmc5jo/6giWawuFj5jFZRx69CcMSx1VaEJ5lS4LmAi5sXuQIDAQAB\",\n" +
                        "\"resource\": \"vertx\",\n" +
                        "\"use-resource-role-mappings\": true,\n" +
                        "\"credentials\": {\n" +
                        "\"secret\": \"eda18747-3d11-456f-a553-d8e140cfaf58\",\n" +
                        "\"jwtToken\": false\n" +
                        "}\n" +
                        "}")
                val oauth2 = KeycloakAuth
                        .create(vertx, OAuth2FlowType.AUTH_CODE, keycloakJson)

//                val handler = OAuth2AuthHandler.create(oauth2,
//                        "http://localhost:8081/")
                val handler = OAuth2AuthHandlerImpl(oauth2, "http://localhost:8081/")
                routerFactory.addSecurityHandler("OAuth2", handler)

                handler.setupCallback(routerFactory.router.route("/callback"))

                routerFactory.addHandlerByOperationId(EndpointUriOperationId.SIGN_UP.endpoint, { routingContext -> signUpRouter?.signUp(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.SIGN_IN.endpoint, { routingContext -> signInRouter?.signIn(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.CONFIRM_ACCESS_CODE.endpoint, { routingContext -> signUpRouter?.confirmAccessCode(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.IS_EMAIL_EXISTS.endpoint, { routingContext -> signUpRouter?.isEmailExists(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.GET_ACCESS_CODE.endpoint, { routingContext -> signUpRouter?.getAccessCode(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.VERIFICATION.endpoint, { routingContext -> signInRouter?.verification(routingContext) })

                // Add an handler with a combination of HttpMethod and path
                // Before router creation you can enable or disable mounting a default failure handler for ValidationException
//                routerFactory.enableValidationFailureHandler(false)
                // Now you have to generate the router

                val router = routerFactory.router
                router.route().handler(CorsHandler.create("*")
                        .allowedMethod(HttpMethod.GET)
                        .allowedMethod(HttpMethod.DELETE)
                        .allowedMethod(HttpMethod.POST)
                        .allowedMethod(HttpMethod.PUT)
                        .allowedMethod(HttpMethod.PATCH)
                        .allowedHeader("Authorization")
                        .allowedHeader("user-agent")
                        .allowedHeader("Access-Control-Request-Method")
                        .allowedHeader("Access-Control-Allow-Credentials")
                        .allowedHeader("Access-Control-Allow-Origin")
                        .allowedHeader("Access-Control-Allow-Headers")
                        .allowedHeader("Content-Type")
                        .allowedHeader("Content-Length")
                        .allowedHeader("X-Requested-With")
                        .allowedHeader("x-auth-token")
                        .allowedHeader("Location")
                        .exposedHeader("Location")
                        .exposedHeader("Content-Type")
                        .exposedHeader("Content-Length")
                        .exposedHeader("ETag")
                        .maxAgeSeconds(60))
                // Now you can use your Router instance
                val server = vertx.createHttpServer(HttpServerOptions().setPort(8081).setHost("0.0.0.0"))
                server.requestHandler(router::accept).listen()
                print("server is run\n")
            } else {
                // Something went wrong during router factory initialization
                val exception = openAPI3RouterFactoryAsyncResult.cause()
                print("OpenApiRoutingError! $exception")
            }
        }

    }

    fun test(routingContext: RoutingContext) {
        routingContext.next()
    }

    override fun stop() {
        super.stop()
        server?.close()
    }
}