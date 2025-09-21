package com.fakepepsilol.grasp.ui.editPage

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.fakepepsilol.grasp.data.ObservableList
import com.fakepepsilol.grasp.data.UrlEntry

class EntriesPreviewProvider: PreviewParameterProvider<ObservableList<UrlEntry>>{
    override val values: Sequence<ObservableList<UrlEntry>>
        get() = sequenceOf(
            ObservableList<UrlEntry>(mutableStateListOf(
                UrlEntry("https://raspored.rs/pub/?pid=tzb"),
                UrlEntry("https://raspored.rs/pub/?pid=mfs&v=o"),
            ))
        )
}