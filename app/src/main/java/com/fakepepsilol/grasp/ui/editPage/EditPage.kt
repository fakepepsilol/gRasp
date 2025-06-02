package com.fakepepsilol.grasp.ui.editPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fakepepsilol.grasp.ui.Pages

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
    val preview_entries: MutableList<String> = mutableListOf(
        "https://raspored.rs/pub/?pid=tzb",
        "https://raspored.rs/pub/?pid=el0")
    val context = LocalContext.current

    @Suppress("unused_variable")
    val page = Pages.Edit
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,

        ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val entries = if (isPreview) preview_entries else viewModel.config.urls
            entries.forEach {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            FloatingActionButton(
                onClick = {
                    if (!isPreview) {
                        viewModel.addEntry("new entry", context)
                    }
                },

                ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        }
    }
}