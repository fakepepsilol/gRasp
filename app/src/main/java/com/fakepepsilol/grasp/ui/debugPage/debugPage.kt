package com.fakepepsilol.grasp.ui.debugPage


import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fakepepsilol.grasp.ui.Pages
import com.fakepepsilol.grasp.ui.editPage.Entry

@Preview
@Composable
private fun DebugPagePreview() {
    DebugPage(preview = true)
}

@Composable
fun DebugPage(preview: Boolean = false) {
    val page = Pages.Settings
    val viewModel: debugViewModel? = if (!preview) {
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
        DebugContents()
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
fun DebugContents() {
    Card(
        Modifier
            .fillMaxWidth(0.90f)
            .fillMaxHeight()
            .padding(bottom = 20.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = DrawerDefaults.modalContainerColor.copy(alpha = 0.8f)
        )
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(10.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            colors = CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.background,
            ),

        ){
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text("debug menu", fontSize = 20.sp, textDecoration = TextDecoration.Underline)
                Spacer(modifier = Modifier.height(5.dp))
                DebugListContents()
            }
        }
    }
}

@Composable
fun DebugListContents() {


    val contents = rememberSaveable { mutableStateListOf<Entry>().apply {
        addAll(List(15) { Entry(it) })
    }  }

    val maxItemCount: Int = 15
    val itemHeight: Dp = 50.dp
    val contentPadding = 5.dp
    val textWeight: Float = 50f
    val deleteButtonWeight: Float = 40f
    val animateHeight = animateDpAsState(
        targetValue =
            if(contents.count() >= maxItemCount)
                (itemHeight * maxItemCount + contentPadding)
            else
                (itemHeight * contents.count() + contentPadding * 2),
        animationSpec = tween(350))
    LazyColumn (
        modifier = Modifier
            .height(animateHeight.value)
            .width(120.dp)
            .border(color = Color.Black, width = 3.dp, shape = RoundedCornerShape(7.dp)),
        contentPadding = PaddingValues(contentPadding)
    ){
        items(items = contents, key = {entry -> entry.id}) { entry ->
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .animateItem(placementSpec = tween(350))
                    .padding(0.dp)
            ){
                Text(modifier = Modifier
                    .weight(textWeight)
                    , text = "id: ${entry.id}",
                    textAlign = TextAlign.Center)
                IconButton (
                    modifier = Modifier
                        .weight(deleteButtonWeight )
                        .padding(0.dp),
                    onClick = {
                        contents.remove(entry)
                    },
                    content = {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }

                )
            }
        }
    }
}