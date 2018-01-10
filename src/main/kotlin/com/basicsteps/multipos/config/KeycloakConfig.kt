package com.basicsteps.multipos.config

object KeycloakConfig {
    val REALM = "master"
    val CLIENT_ID = "vertx"
    val ACCESS_TOKEN_PATH = "http://localhost:8080/auth/realms/master/protocol/openid-connect/token"
    val USER_INFO_PATH = "http://localhost:8080/auth/realms/master/protocol/openid-connect/userinfo"
}