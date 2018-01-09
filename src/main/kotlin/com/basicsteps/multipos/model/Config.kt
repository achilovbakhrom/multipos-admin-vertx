package com.basicsteps.multipos.model

data class Config
    constructor(val host: String, val port: Int, val mongoUri: String, val signUpDbName: String, val signInDbName: String, val keycloakAdimn: String,
                val keycloakAdminPassword: String)