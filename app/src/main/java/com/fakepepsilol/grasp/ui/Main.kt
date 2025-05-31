package com.fakepepsilol.grasp.ui

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.fakepepsilol.grasp.ui.homePage.HomePage
import com.fakepepsilol.grasp.ui.editPage.EditPage
import com.fakepepsilol.grasp.ui.settingsPage.SettingsPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.cancellation.CancellationException


enum class Pages(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    Home(
        label = "Home",
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    Edit(
        label = "Edit",
        selectedIcon = Icons.Default.Edit,
        unselectedIcon = Icons.Outlined.Edit
    ),
    Settings(
        label = "Settings",
        selectedIcon = Icons.Default.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}

@Composable
fun Main() {

    val activity: Activity? = LocalActivity.current
    BackHandler {
        activity?.moveTaskToBack(true)
    }


    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var currentPage: Pages by rememberSaveable { mutableStateOf(Pages.Home) }


    when (currentPage) {
        Pages.Home -> HomePage()
        Pages.Edit -> EditPage()
        Pages.Settings -> SettingsPage()
    }
    NavDrawer(drawerState, scope, currentPage) { selectPage ->
        currentPage = selectPage
        scope.launch {
            drawerState.close()
        }
    }
}


@Composable
fun NavDrawer(
    drawerState: DrawerState,
    scope: CoroutineScope,
    currentPage: Pages,
    onPageSelected: (Pages) -> Unit
) {
    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContents(currentPage, onPageSelected)
        },
    ) {
        if (drawerState.isOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                val released = try {
                                    tryAwaitRelease()
                                } catch (c: CancellationException) {
                                    false
                                }
                                if (released) {
                                    scope.launch { drawerState.close() }
                                } // thanks, https://stackoverflow.com/a/71565747
                            }
                        )
                    }
            )
        }
        val maxDrawerWidthPx = with(LocalDensity.current) { 300.dp.toPx() }
        val percentOpen: Float = if (maxDrawerWidthPx != 0f) {
            (drawerState.currentOffset + maxDrawerWidthPx) / maxDrawerWidthPx
        } else {
            0f
        }

        val shadowModifier =
            Modifier
                .width(100.dp)
                .offset(x = (-20).dp)
                .fillMaxHeight()
                .let { modifier ->
                    modifier.background(
                        brush = Brush.horizontalGradient(
                            colorStops = arrayOf(
                                0f to MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 1f * percentOpen),
                                0.2f to MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 1f * percentOpen),
                                .65f to MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0f)
                            )
                        )
                    ).takeIf { isSystemInDarkTheme() } ?: modifier
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            val released = try {
                                tryAwaitRelease()
                            } catch (c: CancellationException) {
                                false
                            }
                            if (released) {
                                scope.launch {
                                    if (drawerState.isOpen) {
                                        drawerState.close()
                                    } else {
                                        drawerState.open()
                                    }
                                }
                            } // thanks, https://stackoverflow.com/a/71565747
                        }
                    )
                }
        Box(
            modifier = shadowModifier
        )
    }
}

@Composable
fun NavigationDrawerContents(currentPage: Pages, onPageSelected: (Pages) -> Unit) {
    ModalDrawerSheet(
        modifier = Modifier
            .width(300.dp)
    ) {
        HorizontalDivider()
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontSize = 24.sp)) {
                    append("g")
                }
                withStyle(SpanStyle(fontSize = 30.sp)) {
                    append("Rasp")
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 24.dp),
            fontSize = 30.sp,
            style = MaterialTheme.typography.titleLarge
        )
        HorizontalDivider()

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .padding(
                    start = WindowInsets.displayCutout.asPaddingValues()
                        .calculateLeftPadding(LocalLayoutDirection.current) + 4.dp,
                    end = 4.dp
                ),
        ) {
            for (page in Pages.entries) {
                PageEntry(
                    targetPage = page,
                    currentPage = currentPage,
                    onPageSelected = onPageSelected
                )
            }
        }
    }
}

@Composable
fun PageEntry(
    targetPage: Pages,
    currentPage: Pages,
    onPageSelected: (Pages) -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                color = MaterialTheme.colorScheme.onBackground,
                text = targetPage.label
            )
        },
        icon = {
            val icon = if (currentPage == targetPage) {
                targetPage.selectedIcon
            } else {
                targetPage.unselectedIcon
            }
            Icon(imageVector = icon, contentDescription = null)
        },
        selected = currentPage == targetPage,
        onClick = { onPageSelected(targetPage) }
    )
}


@Preview
@Composable
fun NavDrawerPreview() {
    NavigationDrawerContents(
        currentPage = Pages.Home
    ) {}
}

@Preview
@Composable
fun HomePagePreview() {
    HomePage()
}

@Preview
@Composable
fun EditPagePreview() {
    EditPage()
}

@Preview
@Composable
fun SettingsPagePreview() {
    SettingsPage()
}