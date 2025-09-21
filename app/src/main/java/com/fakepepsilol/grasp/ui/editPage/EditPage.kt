package com.fakepepsilol.grasp.ui.editPage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.fakepepsilol.grasp.data.ObservableList
import com.fakepepsilol.grasp.data.UrlEntry
import com.fakepepsilol.grasp.ui.Pages
import com.fakepepsilol.grasp.ui.reusable.ListColumn


const val tag = "EditPageUI"
val page = Pages.Edit
@Composable
fun EditPage() {
    val viewModel: EditViewModel = hiltViewModel()
    val entries: ObservableList<UrlEntry> = viewModel.entries
    EditPageContents(entries)
}
//@Preview
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


@Preview
@Composable
fun EntryCreationDialog(
    onDismiss: () -> Unit = {},
    onSuccess: (UrlEntry) -> Unit = {}
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
                    .height(250.dp),
            ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    val urlTFContents = remember { mutableStateOf("") }
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp, 24.dp, 4.dp, 4.dp),

                        value = urlTFContents.value,
                        onValueChange = { urlTFContents.value = it },
                        singleLine = true
                    )
//                    TextField(
//                        modifier = Modifier
//                            .height(50.dp)
//                            .padding(top = 16.dp),
//                        value = urlTFContents.value,
//                        placeholder = {
//                            Text(
//                                "Rasp URL",
//                                color = MaterialTheme.colorScheme.onBackground
//                            )
//                        },
//                        onValueChange = { newVal -> urlTFContents.value = newVal })
                    Spacer(Modifier.weight(1f))
                    DialogButtons(onDismiss, onDismiss)
                }
            }
        }
    }
}
@Composable fun DialogButtons(
    onCancel: () -> Unit,
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
        FilledTonalButton(
            modifier = Modifier.weight(0.5f),
            shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 24.dp),
            onClick = onCancel
        ) {
            Text(modifier = Modifier.padding(start = 4.dp), text = "Cancel")
        }
        Spacer(Modifier.width(8.dp))
        Button(
            modifier = Modifier.weight(0.5f),
            shape = RoundedCornerShape(0.dp, 0.dp, 24.dp, 0.dp),
            onClick = onConfirm
        ) {
            Text(modifier = Modifier.padding(end = 4.dp), text = "Confirm")
        }
    }
}

