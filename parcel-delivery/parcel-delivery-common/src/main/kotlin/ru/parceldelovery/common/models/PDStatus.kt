package ru.parceldelovery.common.models

/**
 * Статус посылки
 */
enum class PDStatus(val value: String) {

    /** Принято */
    ACCEPTED("ACCEPTED"),

    /** В пути */
    IN_TRANSIT("IN_TRANSIT"),

    /** Ожидает вручения */
    PENDING_DELIVERY("PENDING_DELIVERY"),

    /** Доставлено */
    DELIVERED("DELIVERED"),

    /** Не определено */
    NONE("NONE");

    override fun toString(): String = value
}