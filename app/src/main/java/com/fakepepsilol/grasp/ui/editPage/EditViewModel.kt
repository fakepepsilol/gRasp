package com.fakepepsilol.grasp.ui.editPage

import androidx.lifecycle.ViewModel
import com.fakepepsilol.grasp.data.Config
import com.fakepepsilol.grasp.data.ObservableList
import com.fakepepsilol.grasp.data.ObservableListSerializer
import com.fakepepsilol.grasp.data.UrlEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    @Suppress("unused_variable")
    val config: Config
) : ViewModel() {

    @Suppress("PropertyName", "unused")
    val TAG = "fpl->EditViewModel"

    val entries: ObservableList<UrlEntry> = ObservableList<UrlEntry>(
        onChange = {
            val serializedUrlEntries = Json.encodeToString(ObservableListSerializer, entries)
            CoroutineScope(Dispatchers.IO).launch {
                config.saveString(Config.KEY_UrlEntries, serializedUrlEntries)
            }
        }
    ).apply{ addAll(config.preloaded.urlEntries) }
}