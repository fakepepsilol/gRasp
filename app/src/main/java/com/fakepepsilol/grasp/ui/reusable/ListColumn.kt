package com.fakepepsilol.grasp.ui.reusable

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.fakepepsilol.grasp.data.Entry
import com.fakepepsilol.grasp.data.ObservableList



@Suppress("unused")
@Composable
fun <T : Entry> ListColumn(
    items: ObservableList<T>,
    maxItemCount: Int = 15,
    itemHeight: Dp,
    width: Dp,
    contentPadding: Dp = 5.dp,
    textWeight: Float = 50f,
    buttonWeight: Float = 40f,
    buttonIcon: ImageVector = Icons.Default.Delete,
    resizeAnimation: AnimationSpec<Dp> = tween(350),
    placementAnimation: FiniteAnimationSpec<IntOffset>? = tween(350),
    ){

    val lazyColumnHeight by animateDpAsState(
        targetValue =
            if(items.count() >= maxItemCount)
                contentPadding + (itemHeight * maxItemCount)
            else
                (2 * contentPadding) + (itemHeight * items.count()),
        animationSpec = resizeAnimation
    )
    LazyColumn(
        modifier = Modifier
            .height(lazyColumnHeight)
            .width(width),
        contentPadding = PaddingValues(contentPadding)
    ){
        items(items = items, key = {item -> item.id}){ item ->
            Row(
                modifier = Modifier
                    .animateItem(placementSpec = placementAnimation),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    modifier = Modifier
                        .weight(textWeight),
                    textAlign = TextAlign.Center,
                    text = item.name)
                IconButton(
                    modifier = Modifier
                        .weight(buttonWeight),
                    onClick = {items.remove(item)},
                    content = {
                        Icon(imageVector = buttonIcon, contentDescription = null)
                    }
                )
            }
        }
    }
}