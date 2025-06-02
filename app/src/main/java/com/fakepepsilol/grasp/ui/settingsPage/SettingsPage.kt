package com.fakepepsilol.grasp.ui.settingsPage


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fakepepsilol.grasp.ui.Pages
import javax.inject.Inject

@Preview
@Composable
private fun SettingsPagePreview() {
    SettingsPage(preview = true)
}

@Composable
fun SettingsPage(preview: Boolean = false) {
    val page = Pages.Settings
    val viewModel: SettingsViewModel? = if (!preview) {
        hiltViewModel()
    } else {
        null
    }
    val isPreview: Boolean = viewModel == null

    var targetRotation by rememberSaveable { mutableFloatStateOf(0f) }
    val animationState by animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = tween(durationMillis = 500),
        label = "RotationAnimation"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Header()
        SettingsContents()
    }
}


@Composable
fun Header(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(150.dp)
//            .background(MaterialTheme.colorScheme.primaryContainer)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            DrawerDefaults.modalContainerColor,
                            Color.Transparent
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontSize = 36.sp)) {
                        append("g")
                    }
                    withStyle(SpanStyle(fontSize = 45.sp)) {
                        append("Rasp")
                    }
                },
                modifier = Modifier
                    .padding(
                        vertical = WindowInsets.displayCutout.asPaddingValues()
                            .calculateTopPadding()
                    ),
                fontSize = 30.sp,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        HorizontalDivider(
            Modifier
                .fillMaxWidth(0.85f)
                .offset(y = (-20).dp)
        )
    }

}

@Composable
fun SettingsContents() {
    Card(
        Modifier
            .fillMaxWidth(0.90f)
            .fillMaxHeight()
            .padding(bottom = 20.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = DrawerDefaults.modalContainerColor.copy(alpha = 0.8f)
        )
    ) {
        Column() {

        }
    }
}