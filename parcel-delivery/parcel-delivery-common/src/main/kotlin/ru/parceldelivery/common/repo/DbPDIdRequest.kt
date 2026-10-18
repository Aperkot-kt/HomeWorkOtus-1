package ru.parceldelivery.common.repo

import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDParcelId
import ru.parceldelivery.common.models.PDLock

data class DbPDIdRequest(
    val trackNumber: PDParcelId,
    val lock: PDLock = PDLock.NONE,
) {
    constructor(pd: PDParcel) : this(pd.trackNumber, pd.lock)
}
