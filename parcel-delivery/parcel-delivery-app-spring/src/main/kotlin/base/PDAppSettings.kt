package ru.parceldelivery.app.spring.base

import ru.parceldelivery.api.log.v1.PDLoggerProvider
import ru.parceldelivery.app.biz.PDProcessor
import ru.parceldelivery.app.common.IPDAppSettings
import ru.parceldelivery.common.PDCorSettings

data class PDAppSettings(
    override val corSettings: PDCorSettings,
    override val processor: PDProcessor,
    override val loggerProvider: PDLoggerProvider = PDLoggerProvider(),
): IPDAppSettings