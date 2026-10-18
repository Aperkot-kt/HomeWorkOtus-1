package ru.parceldelivery.app.biz.validation

import kotlinx.coroutines.runBlocking
import ru.parceldelivery.app.biz.FakeRepoPD
import ru.parceldelivery.app.biz.PDProcessor
import ru.parceldelivery.common.PDCorSettings
import ru.parceldelivery.common.PDContext
import ru.parceldelivery.common.models.*
import ru.parceldelivery.common.stubs.ContextStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParcelUpdateValidationTest {
    private val processor = PDProcessor(PDCorSettings(FakeRepoPD()))

    private fun validContext() = PDContext(
        command = PDCommand.UPDATE,
        workMode = PDWorkMode.PROD,
        stubCase = ContextStubs.NONE,
        pdRequest = PDParcel(
            trackNumber = PDParcelId("test-id"),
            deliveryAddress = "Test parcel",
            weight = "50.00".toDouble(),
            senderId = PDUserId("test-sender"),
            receiverId = PDUserId("test-receiver"),
        ),
    )

    @Test
    fun `valid UPDATE request has no errors`() = runBlocking {
        val ctx = validContext()
        processor.exec(ctx)
        assertTrue(ctx.errors.isEmpty(), "Expected no errors but got: ${ctx.errors}")
        assertEquals(PDState.FINISHING, ctx.state)
    }

    @Test
    fun `blank id causes validation error`() = runBlocking {
        val ctx = validContext().copy(pdRequest = validContext().pdRequest.copy(trackNumber = PDParcelId("")))
        processor.exec(ctx)
        assertTrue(ctx.errors.any { it.code == "validation-id-empty" })
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `status-only update passes validation`() = runBlocking {
        // PATCH /status передает только trackNumber и status: остальное подтянется из хранилища
        val ctx = validContext().copy(
            pdRequest = PDParcel(
                trackNumber = PDParcelId("test-id"),
                status = PDStatus.IN_TRANSIT,
            ),
        )
        processor.exec(ctx)
        assertTrue(ctx.errors.isEmpty(), "Expected no errors but got: ${ctx.errors}")
        assertEquals(PDState.FINISHING, ctx.state)
    }

    @Test
    fun `blank senderId causes validation error`() = runBlocking {
        val ctx = validContext().copy(pdRequest = validContext().pdRequest.copy(senderId = PDUserId("  ")))
        processor.exec(ctx)
        assertTrue(ctx.errors.any { it.code == "validation-sender-id-empty" })
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `blank receiverId causes validation error`() = runBlocking {
        val ctx = validContext().copy(pdRequest = validContext().pdRequest.copy(receiverId = PDUserId("  ")))
        processor.exec(ctx)
        assertTrue(ctx.errors.any { it.code == "validation-receiver-id-empty" })
        assertEquals(PDState.FAILING, ctx.state)
    }

}
