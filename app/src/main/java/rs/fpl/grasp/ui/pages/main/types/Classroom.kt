package rs.fpl.grasp.ui.pages.main.types

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = Classroom.Serializer::class)
class Classroom(
    val fullName: String,
    val shortName: String,
    val subjectIndex: Int,
    val classes: List<SubjectClass>
) {
    object Serializer : KSerializer<Classroom> {
        override val descriptor: SerialDescriptor
            get() = buildClassSerialDescriptor("ClassroomSerializer")

        override fun serialize(
            encoder: Encoder,
            value: Classroom
        ) {
            TODO("Not yet implemented")
        }

        override fun deserialize(decoder: Decoder): Classroom {
            if(decoder !is JsonDecoder) throw SerializationException("ClassroomSerializer only works with JSON.")
            val jsonArray = decoder.decodeJsonElement().jsonArray
            return Classroom(
                fullName = jsonArray[0].jsonPrimitive.content,
                shortName = jsonArray[1].jsonPrimitive.content,
                subjectIndex = jsonArray[2].jsonPrimitive.int,
                classes = decoder.json.decodeFromJsonElement(ListSerializer(SubjectClass.Serializer), jsonArray[3])
            )
        }

    }
}