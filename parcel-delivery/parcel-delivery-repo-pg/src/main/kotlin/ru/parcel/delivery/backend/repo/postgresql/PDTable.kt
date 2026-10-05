package ru.parcel.delivery.backend.repo.postgresql

import org.jetbrains.exposed.sql.Cast
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.stringLiteral
import ru.parceldelivery.common.models.*

object PDStatusEnumType : ColumnType() {
    override fun sqlType(): String = "pd_status_type"
    override fun nonNullValueToString(value: Any): String = value.toString()
    override fun valueFromDB(value: Any): Any = value
}

class PDTable(tableName: String) : Table(tableName) {
    val trackNumber = text(SqlFields.TRACK_NUMBER)
    val senderId = text(SqlFields.SENDER_ID).nullable()
    val receiverId = text(SqlFields.RECEIVER_ID).nullable()
    val weight = double(SqlFields.WEIGHT).default(0.0)
    val dimensions = text(SqlFields.DIMENSIONS).nullable()
    val status = text(SqlFields.STATUS).nullable()
    val deliveryAddress = text(SqlFields.DELIVERY_ADDRESS).nullable()
    val createdAt = timestampWithTimeZone(SqlFields.CREATED_AT).nullable()
    val updatedAt = timestampWithTimeZone(SqlFields.UPDATED_AT).nullable()
    val lock = text(SqlFields.LOCK).nullable()

    override val primaryKey = PrimaryKey(trackNumber)

    fun from(res: ResultRow) = PDParcel(
        trackNumber = PDParcelId(res[trackNumber]),
        senderId = res[senderId]?.let { PDUserId(it) } ?: PDUserId.NONE,
        receiverId = res[receiverId]?.let { PDUserId(it) } ?: PDUserId.NONE,
        weight = res[weight],
        dimensions = res[dimensions]?.let {
            val (length, width, height) = it.split("x").map { value -> value.toDouble() }
            PDParcelDimensions(length, width, height)
        },
        status = res[status]?.let { PDStatus.valueOf(it) },
        deliveryAddress = res[deliveryAddress] ?: "",
        createdAt = res[createdAt],
        updatedAt = res[updatedAt],
        lock = res[lock]?.let { PDLock(it) } ?: PDLock.NONE,
    )

    fun UpdateBuilder<*>.to(pd: PDParcel, randomUuid: () -> String) {
        this[trackNumber] = pd.trackNumber.asString().takeIf { it.isNotBlank() } ?: randomUuid()
        this[senderId] = pd.senderId.asString().takeIf { it.isNotBlank() }
        this[receiverId] = pd.receiverId.asString().takeIf { it.isNotBlank() }
        this[weight] = pd.weight
        this[dimensions] = pd.dimensions?.let { "${it.length}x${it.width}x${it.height}" }
        val statusName = pd.status?.name
        if (statusName != null) {
            this[status] = Cast(stringLiteral(statusName), PDStatusEnumType)
        } else {
            this[status] = null
        }
        this[deliveryAddress] = pd.deliveryAddress.takeIf { it.isNotBlank() }
        this[createdAt] = pd.createdAt
        this[updatedAt] = pd.updatedAt
        this[lock] = pd.lock.asString().takeIf { it.isNotBlank() }
    }
}
