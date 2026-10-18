package ru.parceldelivery.common.repo

interface IRepoPD {
    suspend fun createPD(rq: DbPDRequest): IDbPDResponse
    suspend fun readPD(rq: DbPDIdRequest): IDbPDResponse
    suspend fun updatePD(rq: DbPDRequest): IDbPDResponse
    suspend fun deletePD(rq: DbPDIdRequest): IDbPDResponse
    suspend fun searchPD(rq: DbPDFilterRequest): IDbPDsResponse
    companion object {
        val NONE = object : IRepoPD {
            override suspend fun createPD(rq: DbPDRequest): IDbPDResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readPD(rq: DbPDIdRequest): IDbPDResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updatePD(rq: DbPDRequest): IDbPDResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deletePD(rq: DbPDIdRequest): IDbPDResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchPD(rq: DbPDFilterRequest): IDbPDsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
