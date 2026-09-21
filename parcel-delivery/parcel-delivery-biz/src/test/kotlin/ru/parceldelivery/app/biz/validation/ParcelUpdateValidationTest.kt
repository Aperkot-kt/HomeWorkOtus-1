package ru.parceldelivery.app.biz.validation

import kotlinx.coroutines.runBlocking
import ru.parceldelivery.app.biz.PDProcessor
import ru.parceldelovery.common.PDContext
import ru.parceldelovery.common.models.*
import ru.parceldelovery.common.stubs.ContextStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParcelUpdateValidationTest {
    private val processor = PDProcessor()

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
    fun `blank delivery address causes validation error`() = runBlocking {
        val ctx = validContext().copy(pdRequest = validContext().pdRequest.copy(deliveryAddress = ""))
        processor.exec(ctx)
        assertTrue(ctx.errors.any { it.code == "validation-delivery-address-empty" })
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `zero weight causes validation error`() = runBlocking {
        val ctx = validContext().copy(pdRequest = validContext().pdRequest.copy(weight = 0.0))
        processor.exec(ctx)
        assertTrue(ctx.errors.any { it.code == "validation-weight-not-positive" })
        assertEquals(PDState.FAILING, ctx.state)
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
