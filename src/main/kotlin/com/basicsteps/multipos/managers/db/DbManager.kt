package com.basicsteps.multipos.managers.db

import com.basicsteps.multipos.managers.db.signUp.SignUpProtocol
import com.basicsteps.multipos.managers.db.signUp.SignUpProtocolImpl
import com.basicsteps.multipos.model.sign_up.SignUpMapper
import com.basicsteps.multipos.utils.JsonConfigurer
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder


class DbManager(vertx: Vertx) {
    var signUpClient: MongoClient? = null
    var usersClient: Keycloak? = null
    var tenantClient: MongoClient? = null
    var currentTenant: String = ""
    var signUpProtocol: SignUpProtocol? = null
    var tenantStore: MongoDataStore? = null
    var signUpStore: MongoDataStore? = null

    var vertx: Vertx? = vertx

    var mongoUri: String? = null

    var currentTenantId: String? = null

    fun createSignUp(signUp: SignUpMapper) : Observable<String>? {
        return signUpProtocol?.createSignUpMapper(signUp)
    }

    fun confirmAccessCode(mail: String, accessCode: Int) : Observable<Boolean>? {
        return signUpProtocol?.accessCode(mail, accessCode)
    }

    fun isEmailUnique(mail: String) : Observable<Boolean>? {
        return signUpProtocol?.emailUnique(mail)
    }

    fun removeSignUpMapper(id: String) : Observable<Boolean>? {
        return signUpProtocol?.removeSignUpMapper(id)
    }

    fun getMongoClientByTenantId(tenantId: String) : MongoDataStore? {
        if (tenantId.equals(currentTenant) && tenantClient != null)
            return tenantStore
        val tenantMongoConfig = JsonObject()
                .put("connection_string", mongoUri)
                .put("db_name", tenantId)
        tenantClient = MongoClient.createShared(vertx, tenantMongoConfig)
        tenantStore = MongoDataStore(vertx, tenantClient, tenantMongoConfig)
        return tenantStore
    }

    fun close() {
        signUpClient?.close()
        tenantClient?.close()
        usersClient?.close()
    }

    init {
        JsonConfigurer.getConfig().setHandler({ event ->
            val config = event.result()
            mongoUri = config.mongoUri
            val signUpMongoConfig = JsonObject()
                    .put("connection_string", mongoUri)
                    .put("db_name", config.signUpDbName)
            signUpClient = MongoClient.createShared(vertx, signUpMongoConfig)

            usersClient = Keycloak.getInstance(
                    "http://localhost:8080/auth",
                    "master", // the realm to log in to
                    "basicsteps", "26091981", "token-client", "f95c67b3-95df-444d-98be-60513de51a0b"
            )
            signUpStore = MongoDataStore(this.vertx, signUpClient, signUpMongoConfig)
            signUpProtocol = SignUpProtocolImpl(this)
        })
    }
}