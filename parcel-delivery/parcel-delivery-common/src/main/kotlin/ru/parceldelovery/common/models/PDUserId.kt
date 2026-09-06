package ru.parceldelovery.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class PDUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PDUserId("")
    }
}
