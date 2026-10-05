package ru.parcel.delivery.backend.repo.postgresql

import com.benasher44.uuid.uuid4
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import ru.parcel.delivery.backend.repo.tests.*
import ru.parcel.delivery.repo.common.PDRepoInitialized
import ru.parcel.delivery.repo.common.IRepoPDInitializable
import ru.parceldelivery.common.models.PDParcel
import java.io.File
import java.time.Duration
import kotlin.test.AfterTest


private fun IRepoPDInitializable.clear() {
    val pgRepo = (this as PDRepoInitialized).repo as RepoPDSql
    pgRepo.clear()
}

class RepoPDSQLTest {

    @Nested
    inner class RepoPDSQLCreateTest : RepoPDCreateTest() {
        override val repo = repoUnderTestContainer(
            initObjects,
            randomUuid = { uuidNew.asString() },
        )

        @AfterTest
        fun tearDown() = repo.clear()
    }

    @Nested
    inner class RepoPDSQLReadTest : RepoPDReadTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    @Nested
    inner class RepoPDSQLUpdateTest : RepoPDUpdateTest() {
        override val repo = repoUnderTestContainer(
            initObjects,
            randomUuid = { lockNew.asString() },
        )

        @AfterTest
        fun tearDown() = repo.clear()
    }

    @Nested
    inner class RepoPDSQLDeleteTest : RepoPDDeleteTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    @Nested
    inner class RepoPDSQLSearchTest : RepoPDSearchTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    companion object {
        private const val PG_SERVICE = "psql"
        private const val MG_SERVICE = "liquibase"

        private val container: ComposeContainer by lazy {
            val res = this::class.java.classLoader.getResource("docker-compose-pg.yml")
                ?: throw Exception("No resource found")
            val file = File(res.toURI())
            ComposeContainer(
                file,
            )
                .withExposedService(PG_SERVICE, 5432)
                .withStartupTimeout(Duration.ofSeconds(300))
                .waitingFor(
                    MG_SERVICE,
                    Wait.forLogMessage(".*Liquibase command 'update' was executed successfully.*", 1)
                )
        }

        private const val HOST = "localhost"
        private const val USER = "postgres"
        private const val PASS = "pd-pass"
        private val PORT by lazy {
            container.getServicePort(PG_SERVICE, 5432) ?: 5432
        }

        fun repoUnderTestContainer(
            initObjects: Collection<PDParcel> = emptyList(),
            randomUuid: () -> String = { uuid4().toString() },
        ): IRepoPDInitializable = PDRepoInitialized(
            repo = RepoPDSql(
                SqlProperties(
                    host = HOST,
                    user = USER,
                    password = PASS,
                    port = PORT,
                ),
                randomUuid = randomUuid
            ),
            initObjects = initObjects,
        )

        @JvmStatic
        @BeforeAll
        fun start() {
            container.start()
        }

        @JvmStatic
        @AfterAll
        fun finish() {
            container.stop()
        }
    }
}