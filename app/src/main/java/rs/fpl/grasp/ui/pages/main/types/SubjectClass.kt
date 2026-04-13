package rs.fpl.grasp.ui.pages.main.types

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = SubjectClass.Serializer::class)
class SubjectClass(

    /**
     * day of the week: 0(monday) - 4(friday)
     */
    val dayOfWeek: Int,

    /**
     * class of the day: 0(first class) - 12(last class)
     */
    val classOfDay: Int,

    /**
     * length in classes
     */
    val length: Int,

    /**
     * class(of people) index
     */
    val students: Students,

    val subjectTypeIndex: Int,

    /**
     * no idea
     */
    val listThing: List<Int>,
) {
    object Serializer : KSerializer<SubjectClass> {
        override val descriptor: SerialDescriptor
            get() = buildClassSerialDescriptor("SubjectClassSerializer")

        override fun serialize(
            encoder: Encoder,
            value: SubjectClass
        ) {
            TODO("Not yet implemented")
//            if (encoder !is JsonEncoder) throw SerializationException("ClassroomSubject Serializer only works with JSON.")
//            encoder.encodeJsonElement(
//                buildJsonArray {
//                    add(encoder.json.encodeToJsonElement(value.dayOfWeek))
//                    add(encoder.json.encodeToJsonElement(value.classOfDay))
//                    add(encoder.json.encodeToJsonElement(value.length))
//                    if (value.classVariantIndex == null) {
//                        add(encoder.json.encodeToJsonElement(value.classIndex))
//                    } else {
//                        add(
//                            buildJsonArray {
//                                buildJsonArray {
//                                    add(encoder.json.encodeToJsonElement(value.classIndex))
//                                    add(encoder.json.encodeToJsonElement(value.classVariantIndex))
//                                }
//                            }
//                        )
//                    }
//                    add(encoder.json.encodeToJsonElement(value.subjectTypeIndex))
//                    add(encoder.json.encodeToJsonElement(value.listThing))
//                }
//            )
        }

        override fun deserialize(decoder: Decoder): SubjectClass {
            if (decoder !is JsonDecoder) throw SerializationException("ClassroomSubject Deserializer only works with JSON.")
            val jsonArray = decoder.decodeJsonElement().jsonArray
            return SubjectClass(
                dayOfWeek = jsonArray[0].jsonPrimitive.int,
                classOfDay = jsonArray[1].jsonPrimitive.int,
                length = jsonArray[2].jsonPrimitive.int,

                students = decoder.json.decodeFromJsonElement(Students.Serializer, jsonArray[3]),

                subjectTypeIndex = jsonArray[4].jsonPrimitive.int,
                listThing = jsonArray[5].jsonArray.map { it.jsonPrimitive.int }
            )
        }
    }
}