package ru.parceldelivery.common.repo

import ru.parceldelivery.common.models.PDStatus
import ru.parceldelivery.common.models.PDUserId

data class DbPDFilterRequest(
    val status: PDStatus? = PDStatus.NONE,

    val senderId: PDUserId = PDUserId.NONE,

    val receiverId: PDUserId = PDUserId.NONE,
)
