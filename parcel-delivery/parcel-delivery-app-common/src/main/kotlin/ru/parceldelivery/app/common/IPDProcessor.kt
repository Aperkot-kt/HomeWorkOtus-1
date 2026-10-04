package ru.parceldelivery.app.common

import ru.parceldelovery.common.PDContext

interface IPDProcessor {
    suspend fun exec(ctx: PDContext)
}
