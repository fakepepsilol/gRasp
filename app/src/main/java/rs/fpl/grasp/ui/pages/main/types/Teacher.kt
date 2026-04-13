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
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = Teacher.Serializer::class)
class Teacher(
    val fullName: String,
    val subjectIndex: Int,
    val classroomIndex: Int,
    val classes: List<SubjectClass>
) {
    object Serializer : KSerializer<Teacher> {
        override val descriptor: SerialDescriptor
            get() = buildClassSerialDescriptor(
                "TeacherSerializer",
                String.serializer().descriptor,
                Int.serializer().descriptor,
                Int.serializer().descriptor
            )

        override fun serialize(
            encoder: Encoder,
            value: Teacher
        ) {
            TODO("Not yet implemented")
        }

        override fun deserialize(decoder: Decoder): Teacher {
            if (decoder !is JsonDecoder) throw SerializationException("TeacherDeserializer only works with JSON.")
            val jsonArray = decoder.decodeJsonElement().jsonArray
            return Teacher(
                fullName = jsonArray[0].jsonPrimitive.content,
                subjectIndex = jsonArray[1].jsonPrimitive.int,
                classroomIndex = jsonArray[2].jsonPrimitive.int,
                classes = decoder.json.decodeFromJsonElement(
                    ListSerializer(SubjectClass.Serializer),
                    jsonArray[3]
                )
            )
        }
    }
}