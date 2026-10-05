package ru.parcel.delivery.repo.common

import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.repo.IRepoPD

interface IRepoPDInitializable: IRepoPD {
    fun save(pds: Collection<PDParcel>) : Collection<PDParcel>
}
