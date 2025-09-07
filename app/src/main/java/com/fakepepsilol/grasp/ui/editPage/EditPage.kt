package com.fakepepsilol.grasp.ui.editPage

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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

    @Suppress("unused_variable", "unused")
    val context = LocalContext.current

    @Suppress("unused_variable", "unused")
    val scope = rememberCoroutineScope()
    val addEntryDialogOpen = remember { mutableStateOf(false) }


    if (addEntryDialogOpen.value) {
        AddEntryDialog(
            viewModel = viewModel,
            onConfirm = {
                if (!isPreview) {
                    viewModel.addEntry(it)
                }
                addEntryDialogOpen.value = false
            },
            onDismiss = {
                addEntryDialogOpen.value = false
            }
        )
    }

//    val listState = rememberLazyListState()
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
                if (index != 0)
                    HorizontalDivider(Modifier.padding(vertical = 4.dp))
                Entry(
                    Modifier.animateItem(fadeInSpec = tween(350)),
                    item = item,
                    index = index,
                    viewModel = viewModel
                )
            }

        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp),
            onClick = {
                addEntryDialogOpen.value = true
            },
            shape = CircleShape,

            ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}

@Preview
@Composable
fun AddEntryDialog(
    viewModel: EditViewModel? = null,
    onDismiss: () -> Unit = {},
    onConfirm: (url: String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val isPreview = viewModel == null
    val url = remember { mutableStateOf("") }
    var confirmEnabled by remember { mutableStateOf(false) }
    var isChecking by remember { mutableStateOf(false) }
    var lastInputTime by remember { mutableLongStateOf(0L) }

    val smoothTime = 300
    val buttonBgAlpha by animateFloatAsState(
        targetValue =
            if (confirmEnabled) 0.0f
            else 0.25f,
        animationSpec = tween(smoothTime)
    )
    val confirmTextColor by animateColorAsState(
        targetValue =
            if (confirmEnabled) MaterialTheme.colorScheme.primary
            else Color.Gray,
        animationSpec = tween(smoothTime)
    )
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp),
            shape = AlertDialogDefaults.shape
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Dialog Icon")
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .onKeyEvent {
                                    if (it.key == Key.Enter && it.type == KeyEventType.KeyUp) {
                                        if (confirmEnabled)
                                            onConfirm(url.value)
                                        true
                                    } else {
                                        false
                                    }
                                },
                            value = url.value,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if (confirmEnabled)
                                        onConfirm(url.value)
                                }
                            ),
                            singleLine = true,
                            onValueChange = {
                                url.value = it
                                confirmEnabled = it.trim().isNotEmpty()
                                if (!isPreview) {
                                    lastInputTime = System.currentTimeMillis()
                                    scope.launch {
                                        delay(1000)
                                        if (System.currentTimeMillis() - lastInputTime >= 1000) {
                                            viewModel.check(
                                                url = url.value,
                                                onStart = {
                                                    isChecking = true
                                                },
                                                onEnd = {
                                                    isChecking = false
                                                }
                                            )
                                        }
                                    }
                                }
                            },
                            label = { Text("URL") },
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text("fact check status: true")
                            if (isChecking) CircularProgressIndicator()
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(66.dp)
                        .padding(bottom = 12.dp)
                        .padding(horizontal = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.5f),

                            onClick = {
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                            )
                        ) {
                            Text("Cancel", color = MaterialTheme.colorScheme.primary)
                        }
                        Button(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.5f),
                            onClick = {
                                onConfirm(url.value)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray.copy(alpha = buttonBgAlpha),
                                disabledContainerColor = Color.Gray.copy(alpha = buttonBgAlpha)
                            ),
                            enabled = confirmEnabled
                        ) {
                            Text(
                                "Confirm",
                                color = confirmTextColor
                            )
                        }
                    }
                }
            }
        }
    }
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
            .height(40.dp)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
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