package com.fakepepsilol.grasp.ui.editPage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import com.fakepepsilol.grasp.data.Config

@HiltViewModel
class EditViewModel @Inject constructor(
    private var config: Config
) : ViewModel() {
    val entries: MutableList<String> = mutableListOf<String>("asd", "123")
}