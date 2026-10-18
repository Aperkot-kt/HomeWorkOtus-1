package ru.parceldelivery.common.repo.exceptions

import ru.parceldelivery.common.models.PDParcelId
import ru.parceldelivery.common.models.PDLock

class RepoConcurrencyException(id: PDParcelId, expectedLock: PDLock, actualLock: PDLock?) : RepoPDException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)