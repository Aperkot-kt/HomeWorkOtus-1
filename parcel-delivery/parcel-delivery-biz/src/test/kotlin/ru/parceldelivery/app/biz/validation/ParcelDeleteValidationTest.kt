package ru.parceldelivery.app.biz.validation

import kotlinx.coroutines.runBlocking
import ru.parceldelivery.app.biz.PDProcessor
import ru.parceldelovery.common.PDContext
import ru.parceldelovery.common.models.PDCommand
import ru.parceldelovery.common.models.PDParcel
import ru.parceldelovery.common.models.PDParcelId
import ru.parceldelovery.common.models.PDState
import ru.parceldelovery.common.models.PDWorkMode
import ru.parceldelovery.common.stubs.ContextStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParcelDeleteValidationTest {
    private val processor = PDProcessor()

    @Test
    fun `valid DELETE request has no errors`() = runBlocking {
        val ctx = PDContext(
            command = PDCommand.DELETE,
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
            command = PDCommand.DELETE,
            workMode = PDWorkMode.PROD,
            stubCase = ContextStubs.NONE,
            pdRequest = PDParcel(trackNumber = PDParcelId("")),
        )
        processor.exec(ctx)
        assertTrue(ctx.errors.any { it.code == "validation-id-empty" })
        assertEquals(PDState.FAILING, ctx.state)
    }
}
