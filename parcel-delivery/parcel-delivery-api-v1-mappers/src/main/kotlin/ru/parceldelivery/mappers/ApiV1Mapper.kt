package ru.parceldelivery.mappers

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import ru.parceldelivery.api.v1.models.IRequest
import ru.parceldelivery.api.v1.models.IResponse


val apiV1Mapper = JsonMapper.builder().run {
    addModule(kotlinModule())
    addModule(JavaTimeModule())
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
    build()
}

@Suppress("unused")
fun apiV1RequestSerialize(request: IRequest): String = apiV1Mapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST", "unused")
fun <T : IRequest> apiV1RequestDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IRequest::class.java) as T

@Suppress("unused")
fun apiV1ResponseSerialize(response: IResponse): String = apiV1Mapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST", "unused")
fun <T : IResponse> apiV1ResponseDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IResponse::class.java) as T