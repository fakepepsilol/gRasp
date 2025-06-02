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
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class EditViewModel @Inject constructor(
    @Suppress("unused_variable")
    val config: Config
) : ViewModel() {
    val TAG = "fpl->EditViewModel"
    val entries: MutableList<String> = mutableStateListOf()

    fun addEntry(newEntry: String, context: Context) {
        config.urls.add("newurl")
//        if (entries.count() >= 10) {
//            Toast.makeText(context, "Maximum number of entries reached.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        entries.add(newEntry)
    }
}