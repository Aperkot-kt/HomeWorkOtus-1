package ru.parceldelivery.app.biz.stubs

import kotlinx.coroutines.runBlocking
import ru.parceldelivery.app.biz.PDProcessor
import ru.parceldelovery.common.PDContext
import ru.parceldelovery.common.models.*
import ru.parceldelovery.common.stubs.ContextStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParcelUpdateStubTest {
    private val processor = PDProcessor()

    private fun stubCtx(stubCase: ContextStubs) = PDContext(
        command = PDCommand.UPDATE,
        workMode = PDWorkMode.STUB,
        stubCase = stubCase,
        pdRequest = PDParcel(
            trackNumber = PDParcelId("test-id"),
            deliveryAddress = "Test parcel",
            weight = "50.00".toDouble(),
            senderId = PDUserId("test-sender"),
            receiverId = PDUserId("test-receiver"),
        ),
    )

    @Test
    fun `UPDATE SUCCESS returns stub parcel`() = runBlocking {
        val ctx = stubCtx(ContextStubs.SUCCESS)
        processor.exec(ctx)
        assertEquals("PD-2026-000001", ctx.parcelRepoDone.trackNumber.asString())
        assertTrue(ctx.errors.isEmpty())
        assertEquals(PDState.FINISHING, ctx.state)
    }

    @Test
    fun `UPDATE VALIDATION_BAD_ID returns error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.VALIDATION_BAD_ID)
        processor.exec(ctx)
        assertEquals("validation-id-empty", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `UPDATE VALIDATION_BAD_DELIVERY_ADDRESS returns error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.VALIDATION_BAD_DELIVERY_ADDRESS)
        processor.exec(ctx)
        assertEquals("validation-delivery-address-empty", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `UPDATE VALIDATION_BAD_WEIGHT returns error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.VALIDATION_BAD_WEIGHT)
        processor.exec(ctx)
        assertEquals("validation-weight-not-positive", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `UPDATE VALIDATION_BAD_sender_ID returns error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.VALIDATION_BAD_SENDER_ID)
        processor.exec(ctx)
        assertEquals("validation-sender-id-empty", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `UPDATE DB_ERROR returns db error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.DB_ERROR)
        processor.exec(ctx)
        assertEquals("db-error", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `UPDATE NO_CASE returns stub-not-found error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.NO_CASE)
        processor.exec(ctx)
        assertEquals("stub-not-found", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }
}
