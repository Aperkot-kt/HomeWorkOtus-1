package ru.parceldelivery.api.log.v1

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class PDLoggerProvider(
    private val provider: (String) -> IPDLogWrapper = { IPDLogWrapper.DEFAULT }
) {
    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(loggerId: String): IPDLogWrapper = provider(loggerId)

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(clazz: KClass<*>): IPDLogWrapper = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(function: KFunction<*>): IPDLogWrapper = provider(function.name)
}
