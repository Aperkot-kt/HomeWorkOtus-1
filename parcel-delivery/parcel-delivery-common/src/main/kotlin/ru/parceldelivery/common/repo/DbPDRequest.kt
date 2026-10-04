package ru.parceldelivery.common.repo

import ru.parceldelivery.common.models.PDParcel

data class DbPDRequest(
    val pd: PDParcel
)
