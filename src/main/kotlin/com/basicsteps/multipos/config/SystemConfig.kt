package com.basicsteps.multipos.config

object SystemConfig {
    val HOST = "0.0.0.0"
    val PORT = 8081
    val SIGN_UP_DB = "sign-up"

    val KEYCLOAK_ADMIN = "basicsteps"
    val KEYCLOAK_PASSWORD = "26091981"
    val WEB_TOKEN_CLIENT_ID = "token-client"
    val WEB_TOKEN_CLIENT_SECRET = "f95c67b3-95df-444d-98be-60513de51a0b"
    val KEYCLOAK_AUTH_ENDPOINT = "http://localhost:8080/auth"

    val COMMON_DB  = "common"
    val POS_TOKEN_CLIENT_ID = "<POS_TOKEN_CLIENT_ID>"
    val POS_TOKEN_CLIENT_SECRET = "<POS_TOKEN_CLIENT_SECRET>"
    val BEARER_CLIENT_ID = "vertx"
    val BEARER_CLIENT_SECRET = "eda18747-3d11-456f-a553-d8e140cfaf58"
    val REALM = "master"
    val ACCESS_TOKEN_PATH = "<ACCESS_TOKEN_PATH>"
    val REFRESH_TOKEN_PATH = "<ACCESS_TOKEN_PATH>"
    val UNITS_PATH = "${System.getProperty("user.dir")}/src/main/resources/units.yaml"
    val COUNTRIES_PATH = "${System.getProperty("user.dir")}/src/main/resources/countries.yaml"
    val CURRENCIES_PATH = "${System.getProperty("user.dir")}/src/main/resources/currencies.yaml"
    val SYSTEM_CREDENTIALS = "mp-system"
    val REALM_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn9Xya697ZVZzQidld4uCwRoWmLyWBDQQhn+EL1e0WDUWq9v39OBpM+HadkYlOMvfU1A8ohGZZVBkKV4w35gkm3bFPluCPsWxdcqD1NNF6BnIC6bRicgP/4beeehff8nWI3mFAfH7Q7Ik8mm8BDQYhOPRx50JBkDiIQ7AlAjNJ+5/eIj6Pt/eZSmMSk+vM4Xu64E0mCZfHpN+VPQejNBz7h9nEdi3swIIo0ot2+5PZGELX/2Dek7cY4RMKGb+rvU6ug3UvZHQ985KuubKsWMCs8A80yWSoA6umw1DC5rAmc5jo/6giWawuFj5jFZRx69CcMSx1VaEJ5lS4LmAi5sXuQIDAQAB"
    val OAUTH2_CALLBACK = "http://localhost:8081/"
    val MONGO_URI = "mongodb://localhost:27017/"
    val BASE_URL = "http://$HOST:$PORT"
}