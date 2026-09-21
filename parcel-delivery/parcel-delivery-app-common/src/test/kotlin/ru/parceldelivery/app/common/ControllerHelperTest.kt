package ru.parceldelivery.app.common

import kotlinx.coroutines.test.runTest
import ru.parceldelivery.api.v1.models.DeliveryCreateRequest
import ru.parceldelivery.api.v1.models.DeliveryCreateResponse
import ru.parceldelivery.api.v1.models.IRequest
import ru.parceldelivery.api.v1.models.IResponse
import ru.parceldelivery.api.v1.models.ParcelDimensions
import ru.parceldelivery.api.v1.models.RequestType
import ru.parceldelivery.api.v1.models.ResponseResult
import ru.parceldelivery.mappers.fromTransport
import ru.parceldelivery.mappers.toTransport
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ControllerHelperTest {

    private val request = DeliveryCreateRequest(
        requestType = RequestType.CREATE,
        senderId = "CL-1001",
        receiverId = "CL-1002",
        weight = 5.5,
        dimensions = ParcelDimensions(40.0, 30.0, 20.0),
    )

    private val appSettings: IPDAppSettings = object : IPDAppSettings {
        override val corSettings: PDCorSettings = PDCorSettings()
        override val processor: PDParcelProcessor = PDParcelProcessor()
    }

    private suspend fun createParcel(request: DeliveryCreateRequest): DeliveryCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransport() as DeliveryCreateResponse },
            ControllerHelperTest::class,
            "controller-helper-test"
        )

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T

        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createParcelAppCall(appSettings: IPDAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<DeliveryCreateRequest>()) },
            { toTransport() },
            ControllerHelperTest::class,
            "controller-helper-test"
        )
        respond(resp)
    }

    @Test
    fun `helper processes create request successfully`() = runTest {
        val res = createParcel(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
        assertTrue(res.trackNumber?.startsWith("PD-") == true, "expected generated track number, got ${res.trackNumber}")
    }

    @Test
    fun `helper works through application call`() = runTest {
        val testApp = TestApplicationCall(request).apply { createParcelAppCall(appSettings) }
        val res = testApp.res as DeliveryCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}