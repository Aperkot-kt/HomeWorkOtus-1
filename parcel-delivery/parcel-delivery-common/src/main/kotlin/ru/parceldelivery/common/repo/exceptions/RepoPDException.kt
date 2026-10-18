package ru.parceldelivery.common.repo.exceptions

import ru.parceldelivery.common.models.PDParcelId

open class RepoPDException(
    @Suppress("unused")
    val pdId: PDParcelId,
    msg: String,
): RepoException(msg)
