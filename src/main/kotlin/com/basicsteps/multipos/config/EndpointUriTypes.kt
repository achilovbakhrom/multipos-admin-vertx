package com.basicsteps.multipos.config

enum class EndpointUriTypes(val endpoint: String) {
    SIGN_UP("sign-up"),
    SIGN_IN("sign-in"),
    CONFIRM_ACCESS_CODE("confirm-access-code"),
    IS_EMAIL_EXISTS("check-email"),
    GET_ACCESS_CODE("get-access-code"),
    VERIFICATION("verification")
}