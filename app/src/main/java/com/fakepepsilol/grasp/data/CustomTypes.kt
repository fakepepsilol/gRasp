package com.fakepepsilol.grasp.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ObservableList<T>(
    val list: SnapshotStateList<T> = mutableStateListOf(),
    private var onChange: () -> Unit = {}
) : MutableList<T> by list {
    val TAG = "fpl->ObservableList"
    override fun add(element: T): Boolean {
        Log.d(TAG, "added element: $element")
        val result = list.add(element)
        if (result) onChange()
        return result
    }

    override fun remove(element: T): Boolean {
        Log.d(TAG, "removed element: $element")
        val result = list.remove(element)
        if (result) onChange()
        return result
    }

    override fun removeAt(index: Int): T {
        Log.d(TAG, "removed element: ${list[index]}")
        val result = list.removeAt(index)
        onChange()
        return result
    }
}

@Serializable
data class UrlEntry(
    val url: String,
    @kotlinx.serialization.Transient
    override var id: Int = 0,
    override var name: String = "",

    @Serializable(with = LocalDateTimeSerializer::class)
    var lastCheck: LocalDateTime = LocalDateTime.now(),

    @Serializable(with = LocalDateTimeSerializer::class)
    var lastChange: LocalDateTime = LocalDateTime.now(),
) : Entry{
    companion object{
        var maxId = 0
    }
    init {
        name = url
        id = maxId
        maxId++
    }
}
interface Entry{
    val name: String
    val id: Int
}