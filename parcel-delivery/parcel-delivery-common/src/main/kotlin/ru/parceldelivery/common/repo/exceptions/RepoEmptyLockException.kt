package ru.parceldelivery.common.repo.exceptions

import ru.parceldelivery.common.models.PDParcelId

class RepoEmptyLockException(id: PDParcelId) : RepoPDException(
    id,
    "Lock is empty in DB"
)