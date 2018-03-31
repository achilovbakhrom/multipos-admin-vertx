package com.basicsteps.multipos.verticles.handling.handler.signIn

import com.basicsteps.multipos.config.KeycloakConfig
import com.basicsteps.multipos.config.SystemConfig
import com.basicsteps.multipos.core.BaseHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.Company
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.sign_in.SignInMapper
import com.basicsteps.multipos.model.sign_in.SignInResponseMapper
import com.basicsteps.multipos.model.sign_in.VerificationMapper
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.oauth2.OAuth2Auth
import io.vertx.ext.auth.oauth2.OAuth2ClientOptions
import io.vertx.ext.auth.oauth2.OAuth2FlowType
import io.vertx.ext.auth.oauth2.OAuth2Response
import io.vertx.ext.auth.oauth2.AccessToken

class SignInHandler(vertx: Vertx, dbManager: DbManager) : BaseHandler(vertx, dbManager) {

    fun verification(message: Message<String>) {
        val username = message.body()
        val users = dbManager.keycloak?.realm(KeycloakConfig.REALM)?.users()?.search(username)

        if (users != null && !users.isEmpty()) {
            val foundUser = users.get(0)
            val tenantId = foundUser.attributes.get("X-TENANT-ID")?.get(0).toString()
            val name = foundUser.firstName + " " + foundUser.lastName
            message.reply(MultiPosResponse(VerificationMapper(username, name, tenantId), null, "Success", HttpResponseStatus.OK.code()).toJson())
        } else
            message.reply(MultiPosResponse<Any>(null, "$username not found", "Error", HttpResponseStatus.UNAUTHORIZED.code()).toJson())

    }

    fun signInHandler(message: Message<String>) {
        val signInMapper = JsonUtils.toPojo<SignInMapper>(message.body())
        val tokenConfig = JsonObject()
                .put("username", signInMapper.username)
                .put("password", signInMapper.password)

        val credentials = OAuth2ClientOptions()
                .setClientID(signInMapper.clientId)
                .setClientSecret(signInMapper.clientSecret)
                .setTokenPath(KeycloakConfig.ACCESS_TOKEN_PATH)
                .setSite(SystemConfig.BASE_URL)

        // Callbacks
        // Save the access token
        val temp = OAuth2Auth.create(vertx, OAuth2FlowType.PASSWORD, credentials)

        temp.authenticate(tokenConfig, {res ->
            if (res.failed()) {
                System.err.println("Access Token Error: " + res.cause().message)
                message.reply(MultiPosResponse<Any>(null, "Wrong details!!!", "error", HttpResponseStatus.BAD_REQUEST.code()).toJson())
            } else {

                // Get the access token object (the authorization code is given from the previous step).
                val token = res.result() as AccessToken
                val data = JsonUtils.toPojo<SignInResponseMapper>(JsonUtils.toJson(token.principal().map))

                token.fetch(KeycloakConfig.USER_INFO_PATH, { event: AsyncResult<OAuth2Response>? ->
                    if (event?.succeeded()!!) {
                        data.mail = event.result().body().toJsonObject().getString("preferred_username")
                        data.imageUrl = dbManager.keycloak?.realm(KeycloakConfig.REALM)?.users()?.search(data.mail)?.get(0)?.attributes?.get("image_url")?.get(0)!!
                        message.reply(MultiPosResponse(data, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    } else {
                        message.reply(MultiPosResponse<Any>(null, event.cause().toString(), StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
            }
        })
    }

    fun addCompany(message: Message<String>) {
        val companyMapper = JsonUtils.toPojo<Company>(message.body())
        TODO("Adding the company to DB")
    }

    fun refreshAccessToken(message: Message<String>) {
        //TODO refresh token
    }

}