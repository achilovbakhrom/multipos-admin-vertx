package com.basicsteps.multipos.config

enum class ProtectedEndpointUri(val endpoint: String) {
    VERIFICATION("/api/v1/protected/verification");

    fun value() = endpoint
}