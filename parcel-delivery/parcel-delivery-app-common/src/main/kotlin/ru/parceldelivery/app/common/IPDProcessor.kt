package ru.parceldelivery.app.common

import ru.parceldelivery.common.PDContext

interface IPDProcessor {
    suspend fun exec(ctx: PDContext)
}
