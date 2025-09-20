package com.fakepepsilol.grasp.ui.settingsPage

import androidx.lifecycle.ViewModel
import com.fakepepsilol.grasp.data.Config
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val config: Config,
) : ViewModel() {

}