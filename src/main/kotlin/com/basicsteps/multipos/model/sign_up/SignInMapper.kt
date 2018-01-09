package com.basicsteps.multipos.model.sign_up

data class SignInMapper(val grantType: String,
                        val clientId: String,
                        val clientSecret: String,
                        val username: String,
                        val password: String) {
    constructor() : this("", "", "", "", "")
}