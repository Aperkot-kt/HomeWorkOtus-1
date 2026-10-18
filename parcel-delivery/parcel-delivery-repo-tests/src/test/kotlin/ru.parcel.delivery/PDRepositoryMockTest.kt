package ru.parcel.delivery.backend.repo.tests

import kotlinx.coroutines.test.runTest
import ru.parcel.delivery.stubs.PDParcelStub
import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.repo.DbPDFilterRequest
import ru.parceldelivery.common.repo.DbPDIdRequest
import ru.parceldelivery.common.repo.DbPDRequest
import ru.parceldelivery.common.repo.DbPDResponseOk
import ru.parceldelivery.common.repo.DbPDsResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PDRepositoryMockTest {
    private val repo = PDRepositoryMock(
        invokeCreatePD = { DbPDResponseOk(PDParcelStub.prepareResult { it.copy(deliveryAddress = "create") }) },
        invokeReadPD = { DbPDResponseOk(PDParcelStub.prepareResult { it.copy(deliveryAddress = "read") }) },
        invokeUpdatePD = { DbPDResponseOk(PDParcelStub.prepareResult { it.copy(deliveryAddress = "update") }) },
        invokeDeletePD = { DbPDResponseOk(PDParcelStub.prepareResult { it.copy(deliveryAddress = "delete") }) },
        invokeSearchPD = { DbPDsResponseOk(listOf(PDParcelStub.prepareResult { it.copy(deliveryAddress = "search") })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createPD(DbPDRequest(PDParcel()))
        assertIs<DbPDResponseOk>(result)
        assertEquals("create", result.data.deliveryAddress)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readPD(DbPDIdRequest(PDParcel()))
        assertIs<DbPDResponseOk>(result)
        assertEquals("read", result.data.deliveryAddress)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updatePD(DbPDRequest(PDParcel()))
        assertIs<DbPDResponseOk>(result)
        assertEquals("update", result.data.deliveryAddress)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deletePD(DbPDIdRequest(PDParcel()))
        assertIs<DbPDResponseOk>(result)
        assertEquals("delete", result.data.deliveryAddress)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchPD(DbPDFilterRequest())
        assertIs<DbPDsResponseOk>(result)
        assertEquals("search", result.data.first().deliveryAddress)
    }

}
