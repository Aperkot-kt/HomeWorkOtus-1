package ru.parceldelivery.common.repo

import ru.parceldelivery.common.helpers.errorSystem

abstract class PDRepoBase: IRepoPD {

    protected suspend fun tryPDMethod(block: suspend () -> IDbPDResponse) = try {
        block()
    } catch (e: Throwable) {
        DbPDResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryPDsMethod(block: suspend () -> IDbPDsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbPDsResponseErr(errorSystem("methodException", e = e))
    }

}
