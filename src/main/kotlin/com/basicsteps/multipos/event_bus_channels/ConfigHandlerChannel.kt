package com.basicsteps.multipos.event_bus_channels

enum class ConfigHandlerChannel(val channel: String) {
    GET_UNIT_CATEGORY("GET_UNIT_CATEGORY"),
    GET_UNIT_CATEGORY_WITH_UNITS("GET_UNIT_CATEGORY_WITH_UNITS"),
    GET_UNITS("GET_UNITS");

    fun value() : String  = channel
}