package com.fakepepsilol.grasp.ui.editPage

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import com.fakepepsilol.grasp.data.Config
import com.fakepepsilol.grasp.data.UrlEntry
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class EditViewModel @Inject constructor(
    @Suppress("unused_variable")
    val config: Config
) : ViewModel() {
    val TAG = "fpl->EditViewModel"
    //val entries: MutableList<String> = mutableStateListOf()

    fun addEntry(newEntry: String, context: Context) {
        config.urls.add(UrlEntry("item number ${config.urls.size}", maxId + 1))
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
}