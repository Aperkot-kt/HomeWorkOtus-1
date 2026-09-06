package ru.parceldelivery.api.log.v1.mapper

import ru.parceldelovery.common.PDContext
import ru.parceldelovery.common.models.LogLevel
import ru.parceldelovery.common.models.PDError
import ru.parceldelovery.common.models.PDFilter
import ru.parceldelovery.common.models.PDParcel
import ru.parceldelovery.common.models.PDParcelId
import ru.parceldelovery.common.models.PDRequestId
import ru.parceldelovery.common.models.PDStatus
import ru.parceldelovery.common.models.PDUserId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PDContext2LogTest {

    @Test
    fun `empty context produces empty log`() {
        val log = PDContext().toLog("log-1")

        assertEquals("log-1", log.logId)
        assertEquals("parcel-delivery-app", log.source)
        assertTrue(log.messageTime!!.isNotBlank())
        assertNull(log.parcel)
        assertTrue(log.errors!!.isEmpty())
    }

    @Test
    fun `request parcel, filter and errors are mapped`() {
        val ctx = PDContext().apply {
            requestId = PDRequestId("req-1")
            pdRequest = PDParcel(
                trackNumber = PDParcelId("PD-2026-000001"),
                senderId = PDUserId("CL-1001"),
                receiverId = PDUserId("CL-1002"),
                weight = 5.5,
                status = PDStatus.IN_TRANSIT,
                deliveryAddress = "г. Санкт-Петербург, ул. Невский пр., д. 10",
            )
            pdFilterRequest = PDFilter(status = PDStatus.IN_TRANSIT, senderId = "CL-1001")
            errors.add(PDError(code = "NOT_FOUND", field = "trackNumber", message = "not found", level = LogLevel.ERROR))
        }

        val log = ctx.toLog("log-2")
        val pdLog = log.parcel!!

        assertEquals("req-1", pdLog.requestId)
        assertEquals("PD-2026-000001", pdLog.requestParcel?.trackNumber)
        assertEquals("CL-1001", pdLog.requestParcel?.senderId)
        assertEquals("CL-1002", pdLog.requestParcel?.receiverId)
        assertEquals(5.5, pdLog.requestParcel?.weight)
        assertEquals("IN_TRANSIT", pdLog.requestParcel?.status)
        assertEquals("IN_TRANSIT", pdLog.requestFilter?.status)
        assertEquals("CL-1001", pdLog.requestFilter?.senderId)
        assertNull(pdLog.responseParcel)
        assertNull(pdLog.responseParcels)

        val err = log.errors!!.single()
        assertEquals("NOT_FOUND", err.code)
        assertEquals("trackNumber", err.field)
        assertEquals("not found", err.message)
        assertEquals("ERROR", err.level)
    }

    @Test
    fun `response list parcels are mapped`() {
        val parcel1 = PDParcel(trackNumber = PDParcelId("PD-2026-000001"), status = PDStatus.ACCEPTED)
        val parcel2 = PDParcel(trackNumber = PDParcelId("PD-2026-000002"), status = PDStatus.DELIVERED)

        val log = PDContext().apply {
            pdsResponse.addAll(listOf(parcel1, parcel2))
        }.toLog("log-3")

        val responseParcels = log.parcel!!.responseParcels!!
        assertEquals(
            listOf("PD-2026-000001", "PD-2026-000002"),
            responseParcels.map { it.trackNumber },
        )
    }
}