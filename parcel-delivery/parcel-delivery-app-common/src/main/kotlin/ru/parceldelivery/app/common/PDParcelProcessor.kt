package ru.parceldelivery.app.common

import ru.parceldelivery.common.PDContext
import ru.parceldelivery.common.models.PDCommand
import ru.parceldelivery.common.models.PDError
import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDParcelDimensions
import ru.parceldelivery.common.models.PDParcelId
import ru.parceldelivery.common.models.PDStatus
import ru.parceldelivery.common.models.PDUserId
import java.time.OffsetDateTime
import kotlin.random.Random

/**
 * Заглушка обработки посылок: хранилище не используется,
 * для каждой команды формируется ответ.
 *
 * Предполагается замена на полноценный бизнес-модуль.
 */
class PDParcelProcessor {

    fun exec(context: PDContext) {
        when (context.command) {
            PDCommand.CREATE -> processCreate(context)
            PDCommand.READ -> processRead(context)
            PDCommand.UPDATE -> processUpdate(context)
            PDCommand.DELETE -> {
                // заглушка: удалять нечего, возвращаем успех
            }
            PDCommand.SEARCH -> {
                // заглушка: данных нет, возвращаем пустой список
                context.pdsResponse.clear()
            }
            PDCommand.NONE -> context.errors.add(PDError(code = "UNKNOWN_COMMAND", message = "Неизвестная команда"))
        }
    }

    private fun processCreate(context: PDContext) {
        val now = OffsetDateTime.now()
        context.pdResponse = context.pdRequest.copy(
            trackNumber = generateTrackNumber(),
            status = PDStatus.ACCEPTED,
            createdAt = now,
            updatedAt = now,
        )
    }

    private fun processRead(context: PDContext) {
        // заглушка: хранилища нет, возвращаем NOT_FOUND
        // и посылку-заглушку, чтобы ответить валидным DTO
        context.errors.add(PDError(code = "NOT_FOUND", message = "Посылка с таким номером не найдена"))
        context.pdResponse = stubParcel(context.pdRequest)
    }

    private fun processUpdate(context: PDContext) {
        // заглушка: хранилища нет, возвращаем NOT_FOUND
        // и посылку-заглушку, чтобы ответить валидным DTO
        context.errors.add(PDError(code = "NOT_FOUND", message = "Посылка с таким номером не найдена"))
        context.pdResponse = stubParcel(context.pdRequest)
    }

    private fun stubParcel(request: PDParcel): PDParcel {
        val now = OffsetDateTime.now()
        return request.copy(
            senderId = PDUserId("UNKNOWN"),
            receiverId = PDUserId("UNKNOWN"),
            weight = 0.0,
            dimensions = PDParcelDimensions(0.0, 0.0, 0.0),
            status = request.status ?: PDStatus.ACCEPTED,
            createdAt = now,
            updatedAt = now,
        )
    }

    private fun generateTrackNumber(): PDParcelId =
        PDParcelId("PD-${OffsetDateTime.now().year}-${1_000_000 + Random.nextInt(9_000_000)}")
}