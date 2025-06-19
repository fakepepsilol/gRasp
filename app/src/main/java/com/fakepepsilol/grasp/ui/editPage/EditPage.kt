package com.fakepepsilol.grasp.ui.editPage

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FloatAnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fakepepsilol.grasp.data.ObservableList
import com.fakepepsilol.grasp.data.UrlEntry
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
private fun EditPagePreview() {
    EditPage(true)
}


@Composable
fun EditPage(preview: Boolean = false) {


    val viewModel: EditViewModel? = if (!preview) {
        hiltViewModel()
    } else {
        null
    }


    val isPreview: Boolean = viewModel == null
    val previewEntries: ObservableList<UrlEntry> = remember {
        ObservableList(
            mutableStateListOf(
                UrlEntry("https://raspored.rs/pub/?pid=tzb", 0),
                UrlEntry("https://raspored.rs/pub/?pid=el0", 1)
            ),
        )
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val addEntryDialogOpen = remember { mutableStateOf(false) }


    val listState = rememberLazyListState()
    @Suppress("unused_variable")
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter,

        ) {
        val ySpacer = WindowInsets.displayCutout.asPaddingValues().calculateTopPadding()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            item {
                Spacer(
                    Modifier
                        .height(ySpacer)
                )
            }
            val entries = if (isPreview) previewEntries else viewModel.config.urls
            itemsIndexed(
                items = entries,
                key = { _, item -> item.id },
            ) { index, item ->
                Entry(
                    Modifier.animateItem(),
                    item = item,
                    index = index,
                    viewModel = viewModel
                )
            }

        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {
                if (!isPreview) {
                    viewModel.addEntry("new entry", context)
                }
                scope.launch {
                    delay(20)
                    if (!isPreview) {
                        listState.animateScrollToItem(viewModel.config.urls.size - 1)
                    }
                }
            },
            shape = CircleShape,

            ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}

@Preview
@Composable
fun AddEntryDialog() {
    AlertDialog(
        icon = {
            Icon(Icons.Default.Edit, contentDescription = "Edit Page icon")
        },
        title = {
            Text(text = "dialogTitle")
        },
        text = {
            Text(text = "dialogText")
        },
        onDismissRequest = {
        },
        confirmButton = {
            TextButton(
                onClick = {
//                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
//                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}


@Composable
fun Entry(
    modifier: Modifier = Modifier,
    item: UrlEntry,
    index: Int,
    viewModel: EditViewModel?
) {
    val isPreview: Boolean = viewModel == null
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.url,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Button(
                modifier = Modifier.size(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    if (!isPreview) {
                        viewModel.removeEntryAt(index)
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "remove $item"
                )
            }
        }

    }
}