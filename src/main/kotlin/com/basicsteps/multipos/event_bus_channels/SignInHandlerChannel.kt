package com.basicsteps.multipos.event_bus_channels

enum class SignInHandlerChannel(val channel: String) {
    SIGN_IN("SIGN_IN"),
    VERIFICATION("VERIFICATION"),
    REFRESH_ACCESS_TOKEN("REFRESH_ACCESS_TOKEN");

    fun value() : String  = channel
}