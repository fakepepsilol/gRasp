package com.fakepepsilol.grasp.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fakepepsilol.grasp.ui.debugPage.DebugPage
import com.fakepepsilol.grasp.ui.editPage.EditPage
import com.fakepepsilol.grasp.ui.homePage.HomePage
import com.fakepepsilol.grasp.ui.settingsPage.SettingsPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException


enum class Pages(
    val label: String,
    val contents: @Composable () -> Unit,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    Home(
        label = "Home",
        contents = { HomePage() },
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    Edit(
        label = "Edit",
        contents = { EditPage() },
        selectedIcon = Icons.Default.Edit,
        unselectedIcon = Icons.Outlined.Edit
    ),
    Settings(
        label = "Settings",
        contents = { SettingsPage() },
        selectedIcon = Icons.Default.Settings,
        unselectedIcon = Icons.Outlined.Settings
    ),
    Debug(
        label = "Debug Menu",
        contents = { DebugPage() },
        selectedIcon = Icons.Default.Build,
        unselectedIcon = Icons.Outlined.Build
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


    currentPage.contents()
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
        modifier = Modifier.focusProperties{
            canFocus = drawerState.isOpen
        },
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
                                } catch (_: CancellationException) {
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
                                0.2f to Color.Black.copy(alpha = 0.5f * percentOpen),
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
                            } catch (_: CancellationException) {
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
            .width(300.dp),
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
    EditPage(true)
}

@Preview
@Composable
fun SettingsPagePreview() {
    SettingsPage(true)
}