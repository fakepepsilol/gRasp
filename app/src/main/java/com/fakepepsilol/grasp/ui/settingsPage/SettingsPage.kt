package com.fakepepsilol.grasp.ui.settingsPage


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fakepepsilol.grasp.ui.Pages


@Composable
fun SettingsPage(preview: Boolean = false) {
    val page = Pages.Settings
    val viewModel: SettingsViewModel? = if (!preview) {
        hiltViewModel()
    } else {
        null
    }

    val _preview = viewModel == null
    var targetRotation by rememberSaveable { mutableFloatStateOf(0f) }
    val animationState by animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = tween(durationMillis = 500),
        label = "RotationAnimation"
    )
    val counter = if (!_preview) {
        viewModel.config.counter
    } else {
        69
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "This is the ${page.label} Page",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                modifier = Modifier
                    .size(128.dp)
                    .clickable() {
                        targetRotation += 200f
                        if (!_preview) {
                            viewModel.config.counter++
                        }
                    }
                    .rotate(animationState),
                imageVector = page.selectedIcon,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
            )
            Text(
                text = "You clicked $counter ${
                    if (counter == 1) {
                        "time"
                    } else {
                        "times"
                    }
                }",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}