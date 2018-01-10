package com.basicsteps.multipos.verticles

import com.basicsteps.multipos.config.KeycloakConfig
import com.basicsteps.multipos.core.AbstractDbVerticle
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.event_bus_channels.SignInHandlerChannel
import com.basicsteps.multipos.managers.db.DbManager
import com.basicsteps.multipos.model.sign_in.SignInMapper
import com.basicsteps.multipos.model.sign_in.SignInResponseMapper
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.AsyncResult
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.oauth2.OAuth2Auth
import io.vertx.ext.auth.oauth2.OAuth2ClientOptions
import io.vertx.ext.auth.oauth2.OAuth2FlowType
import io.vertx.ext.auth.oauth2.OAuth2Response
import io.vertx.ext.auth.oauth2.impl.AccessTokenImpl
import io.vertx.ext.auth.oauth2.providers.KeycloakAuth
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.OAuth2AuthHandler


class SignInHandler(dbManager: DbManager) : AbstractDbVerticle(dbManager) {

    var oauth2: OAuth2Auth? = null
    var handler: OAuth2AuthHandler? = null

    override fun start() {
        super.start()
//        initKeycloak()
        initConsumers()
    }

    private fun initKeycloak() {
        val keycloakJson = JsonObject("{\n" +
                "\"realm\": \"master\",\n" +
                "\"bearer-only\": true,\n" +
                "\"auth-server-url\": \"http://localhost:8080/auth\",\n" +
                "\"ssl-required\": \"external\",\n" +
                "\"realm-public-key\": \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn9Xya697ZVZzQidld4uCwRoWmLyWBDQQhn+EL1e0WDUWq9v39OBpM+HadkYlOMvfU1A8ohGZZVBkKV4w35gkm3bFPluCPsWxdcqD1NNF6BnIC6bRicgP/4beeehff8nWI3mFAfH7Q7Ik8mm8BDQYhOPRx50JBkDiIQ7AlAjNJ+5/eIj6Pt/eZSmMSk+vM4Xu64E0mCZfHpN+VPQejNBz7h9nEdi3swIIo0ot2+5PZGELX/2Dek7cY4RMKGb+rvU6ug3UvZHQ985KuubKsWMCs8A80yWSoA6umw1DC5rAmc5jo/6giWawuFj5jFZRx69CcMSx1VaEJ5lS4LmAi5sXuQIDAQAB\",\n" +
                "\"resource\": \"vertx\",\n" +
                "\"use-resource-role-mappings\": true,\n" +
                "\"credentials\": {\n" +
                "\"secret\": \"eda18747-3d11-456f-a553-d8e140cfaf58\"\n" +
                "}\n" +
                "}")

        oauth2 = KeycloakAuth
                .create(vertx, OAuth2FlowType.AUTH_CODE, keycloakJson)

        handler = OAuth2AuthHandler.create(oauth2,
                "http://localhost:8081/")
        val router = Router.router(vertx)
        handler?.setupCallback(router.get("/api/v1/protected/callback"))
        router.route("/api/v1/protected/*").handler(handler)

    }

    private fun initConsumers() {
        vertx.eventBus().consumer<String>(SignInHandlerChannel.SIGN_IN.value(), {
            message: Message<String>? -> this.signInHandler(message!!)
        })
    }

    private fun signInHandler(message: Message<String>) {
        val signInMapper = JsonUtils.toPojo<SignInMapper>(message.body())
        val tokenConfig = JsonObject()
                .put("username", signInMapper.username)
                .put("password", signInMapper.password)

        val credentials = OAuth2ClientOptions()
                .setClientID(signInMapper.clientId)
                .setClientSecret(signInMapper.clientSecret)
                .setTokenPath(KeycloakConfig.ACCESS_TOKEN_PATH)
                .setSite("http://localhost:8081")

        // Callbacks
        // Save the access token
        val temp = OAuth2Auth.create(vertx, OAuth2FlowType.PASSWORD, credentials)
        temp.authenticate(tokenConfig, {res ->
            if (res.failed()) {
                System.err.println("Access Token Error: " + res.cause().message)
                message.reply(MultiPosResponse<Any>(null, "Wrong details!!!", "error", HttpResponseStatus.BAD_REQUEST.code()).toJson())
            } else {
                // Get the access token object (the authorization code is given from the previous step).
                val token = res.result() as AccessTokenImpl
                val data = JsonUtils.toPojo<SignInResponseMapper>(JsonUtils.toJson(token.principal().map))

                token.fetch(KeycloakConfig.USER_INFO_PATH, {event: AsyncResult<OAuth2Response>? ->
                    if (event?.succeeded()!!) {
                        data.mail = event.result().body().toJsonObject().getString("preferred_username")
                        data.username = event.result().body().toJsonObject().getString("name")
                        message.reply(MultiPosResponse(data, null, "Success", HttpResponseStatus.OK.code()).toJson())
                    } else {
                        message.reply(MultiPosResponse<Any>(null, event.cause().toString(), "error", HttpResponseStatus.UNAUTHORIZED.code()).toJson())
                    }
                })
            }
        })
    }



    override fun stop() {
        super.stop()

    }
}