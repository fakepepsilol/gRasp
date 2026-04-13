package rs.fpl.grasp.ui.pages.main.types

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive


@Serializable(StudentSubjectClass.Serializer::class)
class StudentSubjectClass(
    val dayOfWeek: Int,
    val classOfDay: Int,
    val length: Int,
    val byVariants: VariantStudentSubjectClassGroup
) {
    @Serializable(with = VariantStudentSubjectClassGroup.Serializer::class)
    class VariantStudentSubjectClassGroup(
        val variants: List<VariantStudentSubjectClasses>
    ) {
        class VariantStudentSubjectClasses (
            val classes: List<VariantStudentSubjectClass>
        ) {
            object Serializer : KSerializer<VariantStudentSubjectClasses> {
                override val descriptor: SerialDescriptor
                    get() = buildClassSerialDescriptor("VariantStudentSubjectClassesSerializer")

                override fun serialize(
                    encoder: Encoder,
                    value: VariantStudentSubjectClasses
                ) {
                    TODO("Not yet implemented")
                }

                override fun deserialize(decoder: Decoder): VariantStudentSubjectClasses {
                    if(decoder !is JsonDecoder) throw SerializationException("VariantStudentSubjectClassesDeserializer only works with JSON.")
                    val jsonArray = decoder.decodeJsonElement().jsonArray
                    return VariantStudentSubjectClasses(
                        classes = decoder.json.decodeFromJsonElement(ListSerializer(VariantStudentSubjectClass.Serializer), jsonArray)
                    )
                }

            }
        }
        @Serializable(with = VariantStudentSubjectClass.Serializer::class)
        class VariantStudentSubjectClass(
            val subjectType: Int,
            val classrooms: List<Int>,
            val variantIndex: Int,
            val length: Int
        ) {
            object Serializer : KSerializer<VariantStudentSubjectClass> {
                override val descriptor: SerialDescriptor
                    get() = buildClassSerialDescriptor("VariantStudentSubjectClassSerializer")

                override fun serialize(
                    encoder: Encoder,
                    value: VariantStudentSubjectClass
                ) {
                    TODO("Not yet implemented")
                }

                override fun deserialize(decoder: Decoder): VariantStudentSubjectClass {
                    if (decoder !is JsonDecoder) throw SerializationException("VariantStudentSubjectClassSerializer can only be used with JSON.")
                    val jsonArray = decoder.decodeJsonElement().jsonArray
                    return VariantStudentSubjectClass(
                        subjectType = jsonArray[0].jsonPrimitive.int,
                        classrooms = decoder.json.decodeFromJsonElement(ListSerializer(Int.serializer()), jsonArray[1]),
                        variantIndex = jsonArray[2].jsonPrimitive.int,
                        length = jsonArray[3].jsonPrimitive.int
                    )
                }
            }
        }
        object Serializer : KSerializer<VariantStudentSubjectClassGroup> {
            override val descriptor: SerialDescriptor
                get() = buildClassSerialDescriptor("VariantStudentSubjectClassGroupSerializer")

            override fun serialize(
                encoder: Encoder,
                value: VariantStudentSubjectClassGroup
            ) {
                TODO("Not yet implemented")
            }

            override fun deserialize(decoder: Decoder): VariantStudentSubjectClassGroup {
                if(decoder !is JsonDecoder) throw SerializationException("VariantStudentSubjectClassGroupDeserializer only works with JSON")
                return VariantStudentSubjectClassGroup(
                    variants = decoder.decodeJsonElement().jsonArray.map { jsonElement ->
                        decoder.json.decodeFromJsonElement(VariantStudentSubjectClasses.Serializer, jsonElement)
                    }
                )
            }
        }
    }
    object Serializer : KSerializer<StudentSubjectClass> {
        override val descriptor: SerialDescriptor
            get() = buildClassSerialDescriptor("StudentSubjectClassSerializer")

        override fun serialize(
            encoder: Encoder,
            value: StudentSubjectClass
        ) {
            TODO("Not yet implemented")
        }

        override fun deserialize(decoder: Decoder): StudentSubjectClass {
            if(decoder !is JsonDecoder) throw SerializationException("StudentSubjectClassSerializer only works with JSON.")
            val jsonArray = decoder.decodeJsonElement().jsonArray
            return StudentSubjectClass(
                dayOfWeek = jsonArray[0].jsonPrimitive.int,
                classOfDay = jsonArray[1].jsonPrimitive.int,
                length = jsonArray[2].jsonPrimitive.int,
                byVariants = decoder.json.decodeFromJsonElement(VariantStudentSubjectClassGroup.Serializer, jsonArray[3])
            )
        }
    }
}
