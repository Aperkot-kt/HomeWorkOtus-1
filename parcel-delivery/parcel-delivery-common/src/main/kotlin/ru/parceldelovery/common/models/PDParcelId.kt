package ru.parceldelovery.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class PDParcelId(private val id: String) {
    fun asString() = id

    override fun toString(): String = id

    companion object {
        val NONE = PDParcelId("")
    }
}
