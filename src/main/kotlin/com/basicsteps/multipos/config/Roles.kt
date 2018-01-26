package com.basicsteps.multipos.config

enum class Roles(val role: String) {

    SUPER_USER("super_user"),
    MP_ADMIN("mp_admin");

    fun value() : String = role
}