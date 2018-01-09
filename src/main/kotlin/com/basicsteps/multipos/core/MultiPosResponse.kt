package com.basicsteps.multipos.core

import javax.annotation.Nullable


data class MultiPosResponse<T>(@Nullable val body: T, val msg: String) {
    companion object {
        val OK = "OK"
    }
}