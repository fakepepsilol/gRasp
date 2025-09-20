package com.fakepepsilol.grasp.ui.debugPage

import androidx.lifecycle.ViewModel
import com.fakepepsilol.grasp.data.Config
import com.fakepepsilol.grasp.data.ObservableList
import com.fakepepsilol.grasp.data.UrlEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebugViewModel @Inject constructor(
    val config: Config,
) : ViewModel() {
    var urlObservableList: ObservableList<UrlEntry>? = null

    var begun: Boolean = false
    suspend fun timerThingy(){
        if(begun){return}
        begun = true
        while(true) {
            delay(1000)
            urlObservableList?.add(UrlEntry("hello"))
        }
    }
}