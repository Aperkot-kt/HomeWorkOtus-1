package ru.parceldelivery.api

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Тестирование API модуля.
 */

class StubTest {

    @Test
    fun `getVersion should return expected version`() {
        val version = Stub.getVersion()
        assertEquals("0.0.1", version)
    }
}