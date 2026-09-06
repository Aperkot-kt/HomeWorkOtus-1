package ru.parceldelovery.common.models

/**
 * Габариты посылки (длина × ширина × высота в см)
 */
data class PDParcelDimensions(
    /** Длина в см */
    val length: Double,
    /** Ширина в см */
    val width: Double,
    /** Высота в см */
    val height: Double
)