package ru.parcel.delivery.backend.repo.tests

import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.repo.DbPDFilterRequest
import ru.parceldelivery.common.repo.DbPDIdRequest
import ru.parceldelivery.common.repo.DbPDResponseOk
import ru.parceldelivery.common.repo.DbPDsResponseOk
import ru.parceldelivery.common.repo.DbPDRequest
import ru.parceldelivery.common.repo.IDbPDResponse
import ru.parceldelivery.common.repo.IDbPDsResponse
import ru.parceldelivery.common.repo.IRepoPD

class PDRepositoryMock(
    private val invokeCreatePD: (DbPDRequest) -> IDbPDResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeReadPD: (DbPDIdRequest) -> IDbPDResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeUpdatePD: (DbPDRequest) -> IDbPDResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeDeletePD: (DbPDIdRequest) -> IDbPDResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeSearchPD: (DbPDFilterRequest) -> IDbPDsResponse = { DEFAULT_ADS_SUCCESS_EMPTY_MOCK },
): IRepoPD {
    override suspend fun createPD(rq: DbPDRequest): IDbPDResponse {
        return invokeCreatePD(rq)
    }

    override suspend fun readPD(rq: DbPDIdRequest): IDbPDResponse {
        return invokeReadPD(rq)
    }

    override suspend fun updatePD(rq: DbPDRequest): IDbPDResponse {
        return invokeUpdatePD(rq)
    }

    override suspend fun deletePD(rq: DbPDIdRequest): IDbPDResponse {
        return invokeDeletePD(rq)
    }

    override suspend fun searchPD(rq: DbPDFilterRequest): IDbPDsResponse {
        return invokeSearchPD(rq)
    }

    companion object {
        val DEFAULT_AD_SUCCESS_EMPTY_MOCK = DbPDResponseOk(PDParcel())
        val DEFAULT_ADS_SUCCESS_EMPTY_MOCK = DbPDsResponseOk(emptyList())
    }
}
