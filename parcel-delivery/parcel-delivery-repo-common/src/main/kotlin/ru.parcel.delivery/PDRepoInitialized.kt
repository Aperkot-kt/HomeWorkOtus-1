package ru.parcel.delivery.repo.common

import ru.parceldelivery.common.models.PDParcel
import kotlin.collections.toList

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class PDRepoInitialized(
    val repo: IRepoPDInitializable,
    initObjects: Collection<PDParcel> = emptyList(),
) : IRepoPDInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<PDParcel> = save(initObjects).toList()
}
