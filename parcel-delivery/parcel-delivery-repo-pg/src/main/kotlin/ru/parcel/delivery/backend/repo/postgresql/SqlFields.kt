package ru.parcel.delivery.backend.repo.postgresql

object SqlFields {
    const val TRACK_NUMBER = "track_number"
    const val SENDER_ID = "sender_id"
    const val RECEIVER_ID = "receiver_id"
    const val WEIGHT = "weight"
    const val DIMENSIONS = "dimensions"
    const val STATUS = "status"
    const val DELIVERY_ADDRESS = "delivery_address"
    const val CREATED_AT = "created_at"
    const val UPDATED_AT = "updated_at"
    const val LOCK = "lock"

    val allFields = listOf(
        TRACK_NUMBER, SENDER_ID, RECEIVER_ID, WEIGHT, DIMENSIONS, STATUS,
        DELIVERY_ADDRESS, CREATED_AT, UPDATED_AT, LOCK,
    )
}
