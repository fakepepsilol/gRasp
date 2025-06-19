package com.fakepepsilol.grasp.ui.editPage

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.fakepepsilol.grasp.data.Config
import com.fakepepsilol.grasp.data.UrlEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    @Suppress("unused_variable")
    val config: Config
) : ViewModel() {


    @Suppress("PropertyName", "unused")
    val TAG = "fpl->EditViewModel"
    //val entries: MutableList<String> = mutableStateListOf()

    fun addEntry(url: String) {
        config.urls.add(UrlEntry(url, maxId + 1))
        maxId++
    }

    var maxId: Int = 0

    init {
        for (entry in config.urls) {
            if (entry.id > maxId) {
                maxId = entry.id
            }
        }
    }

    fun removeEntryAt(index: Int) {
        config.urls.removeAt(index)
    }

    suspend fun check(
        url: String,
        onStart: () -> Unit = {},
        onEnd: () -> Unit = {},
    ) {
        Log.d(TAG, "beginning check with url: $url")
        onStart()
        onEnd()
    }
}