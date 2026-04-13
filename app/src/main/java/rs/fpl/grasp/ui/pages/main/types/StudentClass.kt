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

@Serializable(with = StudentClass.Serializer::class)
class StudentClass(
    val displayName: String,
    val hexColor: String,
    val supervisorTeacherIndex: Int,
    val classes: List<StudentSubjectClass>
){
    object Serializer : KSerializer<StudentClass> {
        override val descriptor: SerialDescriptor
            get() = buildClassSerialDescriptor("StudentClassSerializer")

        override fun serialize(
            encoder: Encoder,
            value: StudentClass
        ) {
            TODO("Not yet implemented")
        }

        override fun deserialize(decoder: Decoder): StudentClass {
            if(decoder !is JsonDecoder) throw SerializationException("StudentClassSerializerSerializer only works with JSON.")
            val jsonArray = decoder.decodeJsonElement().jsonArray
            return StudentClass(
                displayName = jsonArray[0].jsonPrimitive.content,
                hexColor = jsonArray[1].jsonPrimitive.content,
                supervisorTeacherIndex = jsonArray[2].jsonPrimitive.int,
                classes = decoder.json.decodeFromJsonElement(ListSerializer(StudentSubjectClass.Serializer), jsonArray[3])
            )
        }
    }
}