package ru.parceldelivery.app.biz.stubs

import kotlinx.coroutines.runBlocking
import ru.parceldelivery.app.biz.PDProcessor
import ru.parceldelovery.common.PDContext
import ru.parceldelovery.common.models.*
import ru.parceldelovery.common.stubs.ContextStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParcelCreateStubTest {
    private val processor = PDProcessor()

    private fun stubCtx(stubCase: ContextStubs) = PDContext(
        command = PDCommand.CREATE,
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
    fun `CREATE SUCCESS returns stub parcel`() = runBlocking {
        val ctx = stubCtx(ContextStubs.SUCCESS)
        processor.exec(ctx)
        assertEquals("PD-2026-000001", ctx.parcelRepoDone.trackNumber.asString())
        assertEquals("г. Санкт-Петербург, ул. Невский пр., д. 10", ctx.parcelRepoDone.deliveryAddress)
        assertTrue(ctx.errors.isEmpty())
        assertEquals(PDState.FINISHING, ctx.state)
    }

    @Test
    fun `CREATE VALIDATION_BAD_DELIVERY_ADDRESS returns error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.VALIDATION_BAD_DELIVERY_ADDRESS)
        processor.exec(ctx)
        assertTrue(ctx.errors.isNotEmpty())
        assertEquals("validation-delivery-address-empty", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `CREATE VALIDATION_BAD_WEIGHT returns error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.VALIDATION_BAD_WEIGHT)
        processor.exec(ctx)
        assertTrue(ctx.errors.isNotEmpty())
        assertEquals("validation-weight-not-positive", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `CREATE VALIDATION_BAD_sender_ID returns error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.VALIDATION_BAD_SENDER_ID)
        processor.exec(ctx)
        assertTrue(ctx.errors.isNotEmpty())
        assertEquals("validation-sender-id-empty", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `CREATE DB_ERROR returns db error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.DB_ERROR)
        processor.exec(ctx)
        assertTrue(ctx.errors.isNotEmpty())
        assertEquals("db-error", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `CREATE NO_CASE returns stub-not-found error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.NO_CASE)
        processor.exec(ctx)
        assertTrue(ctx.errors.isNotEmpty())
        assertEquals("stub-not-found", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }
}
