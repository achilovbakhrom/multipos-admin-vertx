package com.basicsteps.multipos.utils

object ValidationUtils {
    fun phoneNumber(phoneNumber: String) : Boolean {
        var regex = "[\\+0-9\\s\\-\\(\\)]+".toRegex()
        return regex.matches(phoneNumber)
    }

    fun email(email: String) : Boolean{
        var regex = "^[a-z0-9._%\\+-]+@[a-z0-9.-]+\\.[a-z]{2,6}\$".toRegex()
        return regex.matches(email.toLowerCase())
    }
}