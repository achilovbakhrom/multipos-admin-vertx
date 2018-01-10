package com.basicsteps.multipos.event_bus_channels

enum class SignInHandlerChannel(val channel: String) {
    SIGN_IN("SIGN_IN");

    fun value() : String  = channel
}