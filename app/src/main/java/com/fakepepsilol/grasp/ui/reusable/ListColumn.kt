package com.fakepepsilol.grasp.ui.reusable

import android.util.Log
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
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
    itemHeight: Dp = 48.dp,
    contentPadding: Dp = 5.dp,
    resizeAnimation: AnimationSpec<Dp> = tween(350),
    placementAnimation: FiniteAnimationSpec<IntOffset>? = tween(350),
    lazyColumnModifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    buttonColor: Color = MaterialTheme.colorScheme.onBackground,
    buttonIcon: ImageVector = Icons.Default.Delete,
    ){
    val tag = "LazyColumn"

    var itemHeight by remember { mutableStateOf(itemHeight) }
    var updatedItemHeight by remember { mutableStateOf(false) }
    val density = LocalDensity.current.density
    val itemCount: Int = items.count()


    val lazyColumnHeight by animateDpAsState(
        targetValue = calculateTargetLazyColumnHeight(
            count = itemCount,
            maxItemCount = maxItemCount,
            itemHeight,
            contentPadding),
        animationSpec = resizeAnimation
    )
//    Log.d(TAG, "lazyColumnHeight: $lazyColumnHeight")
    LazyColumn(
        modifier = Modifier
            .height(lazyColumnHeight)
            .then(lazyColumnModifier),
        contentPadding = PaddingValues(contentPadding),
    ){
        items(items = items, key = {item -> item.id}){ item ->
            Row(
                modifier = Modifier
                    .animateItem(placementSpec = placementAnimation)
                    .fillMaxSize()
                    .then(
                        if(item == items.firstOrNull()){
                        Modifier.onGloballyPositioned{ pos ->
                            if(!updatedItemHeight) {
                                itemHeight = (pos.size.height / density).dp
                                Log.d(tag, "recalculated itemHeight: $itemHeight")
                                updatedItemHeight = true
                            }
                        }} else Modifier),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    modifier = Modifier
                        .weight(1f),
                    style = textStyle,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    text = item.name)
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(40.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(onClick = {items.remove(item)}),
                    contentAlignment = Alignment.Center,
                ){
                    Icon(
                        modifier = Modifier.size(24.dp),
                        tint = buttonColor,
                        imageVector = buttonIcon,
                        contentDescription = null)
                }



            }
        }
    }
}

fun calculateTargetLazyColumnHeight(count: Int, maxItemCount: Int, itemHeight: Dp, contentPadding: Dp): Dp {
//    Log.d("FPL", "Calculating new height..")
    if(count == 0) return 0.dp
    return if(count >= maxItemCount) {
//        Log.d("FPL", "new height: ${contentPadding + (itemHeight * maxItemCount)}")
        contentPadding + (itemHeight * maxItemCount)
    } else {
//        Log.d("FPL", "new height: ${(2 * contentPadding) + (itemHeight * count)}")
        (2 * contentPadding) + (itemHeight * count)
    }
}