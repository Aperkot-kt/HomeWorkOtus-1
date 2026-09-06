package ru.parceldelivery.app.spring.base

import ru.parceldelivery.app.common.IPDAppSettings
import ru.parceldelivery.app.common.PDCorSettings
import ru.parceldelivery.app.common.PDParcelProcessor

data class PDAppSettings(
    override val corSettings: PDCorSettings,
    override val processor: PDParcelProcessor,
): IPDAppSettings