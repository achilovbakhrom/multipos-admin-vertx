package com.basicsteps.multipos.event_bus_channels

enum class CompanyHandlerChannel(val channel: String) {
    GET_COMPANIES_BY_USERNAME("GET_COMPANIES_BY_USERNAME");

    fun value() : String  = channel
}