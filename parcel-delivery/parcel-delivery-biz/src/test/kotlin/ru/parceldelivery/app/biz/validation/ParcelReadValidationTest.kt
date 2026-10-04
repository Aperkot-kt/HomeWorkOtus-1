package ru.parceldelivery.app.biz.validation

import kotlinx.coroutines.runBlocking
import ru.parceldelivery.app.biz.PDProcessor
import ru.parceldelivery.common.PDContext
import ru.parceldelivery.common.models.PDCommand
import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDParcelId
import ru.parceldelivery.common.models.PDState
import ru.parceldelivery.common.models.PDWorkMode
import ru.parceldelivery.common.stubs.ContextStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParcelReadValidationTest {
    private val processor = PDProcessor()

    @Test
    fun `valid READ request has no errors`() = runBlocking {
        val ctx = PDContext(
            command = PDCommand.READ,
            workMode = PDWorkMode.PROD,
            stubCase = ContextStubs.NONE,
            pdRequest = PDParcel(trackNumber = PDParcelId("some-id")),
        )
        processor.exec(ctx)
        assertTrue(ctx.errors.isEmpty(), "Expected no errors but got: ${ctx.errors}")
        assertEquals(PDState.FINISHING, ctx.state)
    }

    @Test
    fun `blank id causes validation error`() = runBlocking {
        val ctx = PDContext(
            command = PDCommand.READ,
            workMode = PDWorkMode.PROD,
            stubCase = ContextStubs.NONE,
            pdRequest = PDParcel(trackNumber = PDParcelId("  ")),
        )
        processor.exec(ctx)
        assertTrue(ctx.errors.any { it.code == "validation-id-empty" })
        assertEquals(PDState.FAILING, ctx.state)
    }
}
