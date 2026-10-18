package ru.parcel.delivery

import ru.parcel.delivery.backend.repo.tests.*
import ru.parcel.delivery.repo.common.PDRepoInitialized
import ru.parcel.delivery.repo.inmemory.PDRepoInMemory

class PDRepoInMemoryCreateTest : RepoPDCreateTest() {
    override val repo = PDRepoInitialized(
        PDRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class PDRepoInMemoryDeleteTest : RepoPDDeleteTest() {
    override val repo = PDRepoInitialized(
        PDRepoInMemory(),
        initObjects = initObjects,
    )
}

class PDRepoInMemoryReadTest : RepoPDReadTest() {
    override val repo = PDRepoInitialized(
        PDRepoInMemory(),
        initObjects = initObjects,
    )
}

class PDRepoInMemorySearchTest : RepoPDSearchTest() {
    override val repo = PDRepoInitialized(
        PDRepoInMemory(),
        initObjects = initObjects,
    )
}

class PDRepoInMemoryUpdateTest : RepoPDUpdateTest() {
    override val repo = PDRepoInitialized(
        PDRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
