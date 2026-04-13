package rs.fpl.grasp.ui.pages.main.types

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

class Students(
    val students: List<VariantStudents>
) {
    class VariantStudents(
        val studentsIndex: Int,
        val studentsVariantIndex: Int?
    )

    object Serializer : KSerializer<Students> {
        override val descriptor: SerialDescriptor
            get() = buildClassSerialDescriptor("StudentsSerializer")

        override fun serialize(
            encoder: Encoder,
            value: Students
        ) {
            TODO("Not yet implemented")
        }

        override fun deserialize(decoder: Decoder): Students {
            if (decoder !is JsonDecoder) throw SerializationException("StudentsSerializer only works with JSON.")
            return when (val jsonObject = decoder.decodeJsonElement()) {
                is JsonPrimitive -> {
                    Students(listOf(VariantStudents(jsonObject.int, null)))
                }

                is JsonArray -> {
                    val variants: MutableList<VariantStudents> = mutableListOf()
                    jsonObject.forEach { innerObject ->
                        when (innerObject) {
                            is JsonPrimitive -> {
                                variants.add(VariantStudents(innerObject.int, null))
                            }

                            is JsonArray -> {
                                variants.add(
                                    VariantStudents(
                                        innerObject[0].jsonPrimitive.int,
                                        if(innerObject.size > 1) {
                                            innerObject[1].jsonPrimitive.int
                                        } else null
                                    )
                                )
                            }

                            else -> {}
                        }
                    }
                    Students(variants.toList())
                }

                else -> throw SerializationException("StudentsSerializer expected Primite or Array, got Object instead.")
            }
        }
    }
}