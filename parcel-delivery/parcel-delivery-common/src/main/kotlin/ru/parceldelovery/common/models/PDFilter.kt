package ru.parceldelovery.common.models

data class PDFilter(
    val status: PDStatus? = null,

    val senderId: String? = null,

    val receiverId: String? = null
) {
    fun deepCopy(): PDFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = PDFilter()
    }
}
