package rs.fpl.grasp.ui.pages.main.types

import kotlinx.serialization.ExperimentalSerializationApi
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
import kotlinx.serialization.json.jsonObject

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = Timetable.Serializer::class)
class Timetable (
    val general: General,
    val subjects: List<Subject>,
    val classrooms: List<Classroom>,
    val teachers: List<Teacher>,
    val classes: List<StudentClass>
){
    object Serializer : KSerializer<Timetable> {
        override val descriptor: SerialDescriptor
            get() = buildClassSerialDescriptor("TimetableSerializer")

        override fun serialize(
            encoder: Encoder,
            value: Timetable
        ) {
            TODO("Not yet implemented")
        }

        override fun deserialize(decoder: Decoder): Timetable {
            if(decoder !is JsonDecoder) throw SerializationException("TimetableSerializer only works with JSON.")
            val jsonObject = decoder.decodeJsonElement().jsonObject
            return Timetable(
                general = decoder.json.decodeFromJsonElement(
                    General.serializer(),
                    jsonObject["general"] ?: throw SerializationException("Missing key: \"general\"")
                ),
                subjects = decoder.json.decodeFromJsonElement(
                    ListSerializer(ListSerializer(String.serializer())),
                    jsonObject["subjects"] ?: throw SerializationException("Missing key: \"subjects\"")
                ).map { Subject(it) },
                classrooms = decoder.json.decodeFromJsonElement(
                    ListSerializer(Classroom.Serializer),
                    jsonObject["rooms"] ?: throw SerializationException("Missing key: \"rooms\"")
                ),
                teachers = decoder.json.decodeFromJsonElement(
                    ListSerializer(Teacher.Serializer),
                    jsonObject["teachers"] ?: throw SerializationException("Missing key: \"teachers\"")
                ),
                classes = decoder.json.decodeFromJsonElement(
                    ListSerializer(StudentClass.Serializer),
                    jsonObject["classes"] ?: throw SerializationException("Missing key: \"classes\"")
                )
            )
        }

    }
}