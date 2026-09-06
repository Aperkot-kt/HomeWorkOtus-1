package ru.parceldelovery.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class PDLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PDLock("")
    }
}
