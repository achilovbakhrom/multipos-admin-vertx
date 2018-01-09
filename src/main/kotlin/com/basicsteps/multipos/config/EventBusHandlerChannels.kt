package com.basicsteps.multipos.config

enum class EventBusHandlerChannels(val channel: String) {
    SIGN_UP("signUp"),
    SIGN_IN("signIn"),
    CONFIRM_ACCESS_CODE("confirmAccessCode"),
    IS_EMAIL_UNIQUE("isEmailUnique")
}