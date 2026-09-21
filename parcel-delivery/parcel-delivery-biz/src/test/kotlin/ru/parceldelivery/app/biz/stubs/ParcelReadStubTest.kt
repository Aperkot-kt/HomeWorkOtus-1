package ru.parceldelivery.app.biz.stubs

import kotlinx.coroutines.runBlocking
import ru.parceldelivery.app.biz.PDProcessor
import ru.parceldelovery.common.stubs.ContextStubs
import ru.parceldelovery.common.PDContext
import ru.parceldelovery.common.models.PDCommand
import ru.parceldelovery.common.models.PDParcel
import ru.parceldelovery.common.models.PDParcelId
import ru.parceldelovery.common.models.PDState
import ru.parceldelovery.common.models.PDWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParcelReadStubTest {
    private val processor = PDProcessor()

    private fun stubCtx(stubCase: ContextStubs) = PDContext(
        command = PDCommand.READ,
        workMode = PDWorkMode.STUB,
        stubCase = stubCase,
        pdRequest = PDParcel(trackNumber = PDParcelId("test-id")),
    )

    @Test
    fun `READ SUCCESS returns stub parcel`() = runBlocking {
        val ctx = stubCtx(ContextStubs.SUCCESS)
        processor.exec(ctx)
        assertEquals("PD-2026-000001", ctx.parcelRepoDone.trackNumber.asString())
        assertTrue(ctx.errors.isEmpty())
        assertEquals(PDState.FINISHING, ctx.state)
    }

    @Test
    fun `READ VALIDATION_BAD_ID returns error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.VALIDATION_BAD_ID)
        processor.exec(ctx)
        assertTrue(ctx.errors.isNotEmpty())
        assertEquals("validation-id-empty", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `READ DB_ERROR returns db error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.DB_ERROR)
        processor.exec(ctx)
        assertTrue(ctx.errors.isNotEmpty())
        assertEquals("db-error", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `READ NO_CASE returns stub-not-found error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.NO_CASE)
        processor.exec(ctx)
        assertTrue(ctx.errors.isNotEmpty())
        assertEquals("stub-not-found", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }
}
