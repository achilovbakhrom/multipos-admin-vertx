package com.basicsteps.multipos.config

enum class EndpointUriTypes(val endpoint: String) {
    SIGN_UP("signUp"),
    SIGN_IN("signIn"),
    CONFIRM_ACCESS_CODE("confirmAccessCode"),
    IS_EMAIL_EXISTS("checkEmail")
}