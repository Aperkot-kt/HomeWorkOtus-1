package ru.parceldelivery.app.biz.stubs

import kotlinx.coroutines.runBlocking
import ru.parceldelivery.app.biz.PDProcessor
import ru.parceldelivery.common.stubs.ContextStubs
import ru.parceldelivery.common.PDContext
import ru.parceldelivery.common.models.PDCommand
import ru.parceldelivery.common.models.PDState
import ru.parceldelivery.common.models.PDWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParcelSearchStubTest {
    private val processor = PDProcessor()

    private fun stubCtx(stubCase: ContextStubs) = PDContext(
        command = PDCommand.SEARCH,
        workMode = PDWorkMode.STUB,
        stubCase = stubCase,
    )

    @Test
    fun `SEARCH SUCCESS returns 2 stub parcels`() = runBlocking {
        val ctx = stubCtx(ContextStubs.SUCCESS)
        processor.exec(ctx)
        assertEquals(2, ctx.parcelsRepoDone.size)
        assertEquals("PD-2026-000001", ctx.parcelsRepoDone[0].trackNumber.asString())
        assertEquals("PD-2026-000002", ctx.parcelsRepoDone[1].trackNumber.asString())
        assertTrue(ctx.errors.isEmpty())
        assertEquals(PDState.FINISHING, ctx.state)
    }

    @Test
    fun `SEARCH DB_ERROR returns db error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.DB_ERROR)
        processor.exec(ctx)
        assertEquals("db-error", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }

    @Test
    fun `SEARCH NO_CASE returns stub-not-found error`() = runBlocking {
        val ctx = stubCtx(ContextStubs.NO_CASE)
        processor.exec(ctx)
        assertEquals("stub-not-found", ctx.errors.first().code)
        assertEquals(PDState.FAILING, ctx.state)
    }
}
