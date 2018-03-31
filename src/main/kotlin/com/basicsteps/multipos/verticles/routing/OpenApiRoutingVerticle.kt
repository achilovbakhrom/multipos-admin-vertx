package com.basicsteps.multipos.verticles.routing

import com.basicsteps.multipos.config.EndpointUriOperationId
import com.basicsteps.multipos.config.SystemConfig
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.verticles.routing.router.ConfigRouter
import com.basicsteps.multipos.verticles.routing.router.SignInRouter
import com.basicsteps.multipos.verticles.routing.router.SignUpRouter

import com.basicsteps.multipos.verticles.routing.handler.TenantSwitcherHandler
import com.basicsteps.multipos.verticles.routing.router.CompanyRouter
import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.oauth2.OAuth2FlowType
import io.vertx.ext.auth.oauth2.providers.KeycloakAuth
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.ext.web.handler.impl.OAuth2AuthHandlerImpl

class OpenApiRoutingVerticle : AbstractVerticle() {
    var server: HttpServer? = null
    var signUpRouter: SignUpRouter? = null
    var signInRouter: SignInRouter? = null
    var configRouter: ConfigRouter? = null
    var companyRouter: CompanyRouter? = null

    var tenantSwitcherHandler: TenantSwitcherHandler? = null
    override fun start() {
        super.start()
        signUpRouter = SignUpRouter(vertx)
        signInRouter = SignInRouter(vertx)
        configRouter = ConfigRouter(vertx)
        companyRouter = CompanyRouter(vertx)

        tenantSwitcherHandler = TenantSwitcherHandler(vertx)
        server = vertx.createHttpServer()
        OpenAPI3RouterFactory.create(vertx, "src/main/resources/api.yaml") { openAPI3RouterFactoryAsyncResult ->
            if (openAPI3RouterFactoryAsyncResult.succeeded()) {
                // Spec loaded with success
                val routerFactory = openAPI3RouterFactoryAsyncResult.result()

                val keycloakJson = JsonObject("{\n" +
//                        "\"realm\": \"${SystemConfig.REALM}\",\n" +
                        "\"bearer-only\": true,\n" +
                        "\"auth-server-url\": \"${SystemConfig.KEYCLOAK_AUTH_ENDPOINT}\",\n" +
                        "\"ssl-required\": \"external\",\n" +
                        "\"realm-public-key\": \"${SystemConfig.REALM_PUBLIC_KEY}\",\n" +
                        "\"resource\": \"${SystemConfig.REALM}\",\n" +
                        "\"use-resource-role-mappings\": true,\n" +
                        "\"credentials\": {\n" +
                            "\"client\": \"${SystemConfig.BEARER_CLIENT_ID}\",\n" +
                            "\"secret\": \"${SystemConfig.BEARER_CLIENT_SECRET}\",\n" +
                            "\"jwtToken\": false\n" +
                        "}\n" +
                        "}")
                val oauth2 = KeycloakAuth
                        .create(vertx, OAuth2FlowType.AUTH_CODE, keycloakJson)

                val handler = OAuth2AuthHandlerImpl(oauth2, SystemConfig.OAUTH2_CALLBACK)

                routerFactory.addSecurityHandler("OAuth2", handler)
                routerFactory.addSecurityHandler("TenantSecurity", {routingContext -> tenantSwitcherHandler?.switchTenant(routingContext) })
                handler.setupCallback(routerFactory.router.route("/callback"))

                //SignIn & SignUp endpoints
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.SIGN_UP.endpoint, { routingContext -> signUpRouter?.signUp(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.SIGN_IN.endpoint, { routingContext -> signInRouter?.signIn(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.CONFIRM_ACCESS_CODE.endpoint, { routingContext -> signUpRouter?.confirmAccessCode(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.IS_EMAIL_EXISTS.endpoint, { routingContext -> signUpRouter?.isEmailExists(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.GET_ACCESS_CODE.endpoint, { routingContext -> signUpRouter?.getAccessCode(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.UPLOAD_AVATAR.endpoint, { routingContext -> signUpRouter?.uploadAvatar(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.VERIFICATION.endpoint, { routingContext -> signInRouter?.verification(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.ADD_COMPANY.endpoint, { routingContext -> signInRouter?.addCompany(routingContext) })

                //Currency list
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.GET_CURRENCY_LIST.endpoint, { routingContext -> configRouter?.getCurrencies(routingContext) })

                //company endpoints
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.GET_COMPANIES_BY_USERNAME.endpoint, {routingContext -> companyRouter?.getCompaniesByUsername(routingContext) })


                //Config endpoints
                //units
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.GET_UNITS.endpoint, {routingContext ->  configRouter?.getUnits(routingContext)})
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.GET_UNIT_CATEGORY_LIST.endpoint, {routingContext ->  configRouter?.getUnitCategories(routingContext)})
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.ACTIVATE_UNIT.endpoint, { routingContext: RoutingContext -> configRouter?.activateUnit(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.DEACTIVATE_UNIT.endpoint, { routingContext: RoutingContext -> configRouter?.deactivateUnit(routingContext) })

                //Account
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.ADD_ACCOUNT.endpoint, { routingContext: RoutingContext -> configRouter?.createAccount(routingContext) })


                //POS & Stock
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.ADD_POS.endpoint, { routingContext: RoutingContext -> configRouter?.addPOS(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.ADD_STOCK.endpoint, { routingContext: RoutingContext -> configRouter?.addStock(routingContext) })
                routerFactory.addHandlerByOperationId(EndpointUriOperationId.LINK_POS_STOCK.endpoint, { routingContext: RoutingContext -> configRouter?.linkPOSStock(routingContext) })

                // Add an handling with a combination of HttpMethod and path
                // Before router creation you can enable or disable mounting a default failure handling for ValidationException
                // Now you have to generate the router

                //CORS accesses
                val router = routerFactory.router
                val bodyHandler = BodyHandler.create("mp-uploads")
                router.route().handler(bodyHandler)

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
                val server = vertx.createHttpServer(HttpServerOptions().setPort(SystemConfig.PORT).setHost(SystemConfig.HOST))
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