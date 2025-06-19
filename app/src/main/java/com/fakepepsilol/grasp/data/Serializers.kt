package com.fakepepsilol.grasp.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object ObservableListSerializer : KSerializer<ObservableList<UrlEntry>> {
    val listSerializer = ListSerializer(UrlEntry.serializer())
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "ObservableList",
        PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: ObservableList<UrlEntry>) {
        encoder.encodeSerializableValue(listSerializer, value.toList())
    }

    override fun deserialize(decoder: Decoder): ObservableList<UrlEntry> {
        val items = decoder.decodeSerializableValue(listSerializer)
        val observableList = ObservableList<UrlEntry>()
        observableList.addAll(items)
        return observableList
    }
}

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}