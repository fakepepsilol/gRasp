package com.fakepepsilol.grasp.ui.editPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fakepepsilol.grasp.ui.Pages

@Preview
@Composable
fun EditPagePreview() {
    EditPage(true)
}


@Composable
fun EditPage(preview: Boolean = false) {


//    val viewModel: EditViewModel = hiltViewModel()
//    val entries: MutableList<String> = viewModel.entries
    val entries: MutableList<String> = mutableListOf("asd", "123")
    //@Suppress("unused")
    val page = Pages.Edit
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,

        ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            entries.forEach {
                Box(
                    modifier = Modifier
                        .width(150.dp)
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
//            Text(
//                text = "This is the ${page.label} Page",
//                style = MaterialTheme.typography.bodyLarge,
//                color = MaterialTheme.colorScheme.onBackground
//            )
//            Icon(
//                modifier = Modifier
//                    .size(128.dp),
//                imageVector = page.selectedIcon,
//                tint = MaterialTheme.colorScheme.primary,
//                contentDescription = null,
//            )

        }
        FloatingActionButton(
            onClick = {
                entries.add("new item")
            },

            ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
        }
    }
}