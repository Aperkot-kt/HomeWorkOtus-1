package ru.parceldelivery.common.repo

import ru.parceldelivery.common.helpers.errorSystem
import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDParcelId
import ru.parceldelivery.common.models.PDLock
import ru.parceldelivery.common.models.PDError
import ru.parceldelivery.common.repo.exceptions.RepoConcurrencyException
import ru.parceldelivery.common.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: PDParcelId) = DbPDResponseErr(
    PDError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbPDResponseErr(
    PDError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldPD: PDParcel,
    expectedLock: PDLock,
    exception: Exception = RepoConcurrencyException(
        id = oldPD.trackNumber,
        expectedLock = expectedLock,
        actualLock = oldPD.lock,
    ),
) = DbPDResponseErrWithData(
    pd = oldPD,
    err = PDError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldPD.trackNumber.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: PDParcelId) = DbPDResponseErr(
    PDError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for PD ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbPDResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)