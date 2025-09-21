@file:OptIn(ExperimentalMaterial3Api::class)

package com.fakepepsilol.grasp.ui.editPage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.fakepepsilol.grasp.data.ObservableList
import com.fakepepsilol.grasp.data.UrlEntry
import com.fakepepsilol.grasp.ui.Pages
import com.fakepepsilol.grasp.ui.reusable.ListColumn
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter


const val tag = "EditPageUI"
val page = Pages.Edit
@Composable
fun EditPage() {
    val viewModel: EditViewModel = hiltViewModel()
    val entries: ObservableList<UrlEntry> = viewModel.entries
    EditPageContents(entries)
}
@Preview
@Composable
fun EditPageContents(
    @PreviewParameter(EntriesPreviewProvider::class)
    entries: ObservableList<UrlEntry>
){
    val density = LocalDensity.current

    val showDialog = remember { mutableStateOf(false) }
    if(showDialog.value){
        EntryCreationDialog(
            onDismiss = {
                showDialog.value = false
            },
            onSuccess = { entry ->
                entries.add(entry)
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(
                modifier = Modifier
                    .height((WindowInsets.statusBars.getTop(density) / density.density).dp))
            HorizontalDivider()
            Log.d(tag, "spacer height: ${(WindowInsets.statusBars.getTop(density) / density.density).dp}")
            ListColumn(
                entries,
            )
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp),
            onClick = {
                entries.add(UrlEntry("https://example/url"))
                showDialog.value = true
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
}


@OptIn(FlowPreview::class)
@Preview
@Composable
fun EntryCreationDialog(
    onDismiss: () -> Unit = {},
    onSuccess: (UrlEntry) -> Unit = {},
    onValueChange: (String) -> Unit = {}
){
    Dialog(onDismissRequest = onDismiss){
        Card(
            modifier = Modifier
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp)
        ){
            Box(
                modifier = Modifier
                    .width(360.dp)
                    .wrapContentHeight(),
            ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
//                    val urlTFContents = remember { mutableStateOf("") }
                    val urlTFState = rememberTextFieldState("")
                    LaunchedEffect(urlTFState.text) {
                        snapshotFlow { urlTFState.text }
                            .debounce(500)
                            .filter { it.isNotEmpty() }
                            .collect { newText ->
                            if(newText.toString().isEmpty()){
                                return@collect
                            }
                            Log.e(tag, newText.toString())
                        }
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, top = 0.dp, end = 8.dp),
                        state = urlTFState,
                        readOnly = false,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        label = @Composable {Text("Rasp URL")},
                        labelPosition = TextFieldLabelPosition.Attached(minimizedAlignment = Alignment.CenterHorizontally, expandedAlignment = Alignment.CenterHorizontally),
                        isError = false,
                        shape = RoundedCornerShape(24.dp, 24.dp, 4.dp, 4.dp),
                        contentPadding = PaddingValues(start = 16.dp, top = 17.25.dp, end = 16.dp),
                    )

                    DialogButtons(onDismiss, {}, onDismiss)
                }
            }
        }
    }
}
@Composable fun DialogButtons(
    onCancel: () -> Unit,
    onRefresh: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
){
    Log.i(tag, "recomposed buttons.")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 4.dp)
            .then(modifier),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Cancel Button
        FilledTonalButton(
            modifier = Modifier.weight(0.5f),
            shape = RoundedCornerShape(4.dp, 4.dp, 4.dp, 24.dp),
            onClick = onCancel
        ) {
            Text(modifier = Modifier.padding(start = 4.dp), text = "Cancel")
        }

        Spacer(Modifier.width(6.dp))

        // Refresh Button
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape( percent = 100 ))
                .clickable(onClick = onRefresh),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null)
        }

        Spacer(Modifier.width(6.dp))

        // Confirm Button
        Button(
            modifier = Modifier.weight(0.5f),
            shape = RoundedCornerShape(4.dp, 4.dp, 24.dp, 4.dp),
            onClick = onConfirm
        ) {
            Text(modifier = Modifier.padding(end = 4.dp), text = "Confirm")
        }
    }
}

