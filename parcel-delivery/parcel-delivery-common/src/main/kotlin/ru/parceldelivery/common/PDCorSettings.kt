package ru.parceldelivery.common

import ru.parceldelivery.common.repo.IRepoPD

/**
 * Инфраструктурные настройки приложения сервиса доставки посылок.
 */
data class PDCorSettings(
    val repo: IRepoPD = IRepoPD.NONE,
) {
    companion object {
        val NONE = PDCorSettings()
    }
}