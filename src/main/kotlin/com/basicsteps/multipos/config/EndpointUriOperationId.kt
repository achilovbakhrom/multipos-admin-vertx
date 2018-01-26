package com.basicsteps.multipos.config

enum class EndpointUriOperationId(val endpoint: String) {
    SIGN_UP("sign-up"),
    SIGN_IN("sign-in"),
    CONFIRM_ACCESS_CODE("confirm-access-code"),
    IS_EMAIL_EXISTS("check-email"),
    GET_ACCESS_CODE("get-access-code"),
    VERIFICATION("verification"),
    GET_UNITS("get-units"),
    GET_UNIT_CATEGORIES_FULL("get-unit-categories-full")
}