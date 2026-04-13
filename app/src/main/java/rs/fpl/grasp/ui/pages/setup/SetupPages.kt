package rs.fpl.grasp.ui.pages.setup

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.fpl.grasp.background.database.GraspDatabase
import rs.fpl.grasp.ui.pages.setup.pages.AddATimetablePage
import rs.fpl.grasp.ui.pages.setup.pages.AllowNotificationsPage
import rs.fpl.grasp.ui.pages.setup.pages.SetupFinishedPage
import rs.fpl.grasp.ui.pages.setup.pages.WelcomePage

@Preview
@Composable
fun SetupPages() {
    val scope = rememberCoroutineScope()
    var page by rememberSaveable { mutableIntStateOf(0) }
    AnimatedContent(
        modifier = Modifier.zIndex(1f),
        targetState = page,
        transitionSpec = {
            if(targetState == 4) {
                slideInVertically { it } togetherWith slideOutVertically { -it }
            } else {
                slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
            }
        }
    ) { targetPage ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when(targetPage) {
                0 -> WelcomePage { page++ }
                1 -> AddATimetablePage { page++ }
                2 -> AllowNotificationsPage { page++ }
                3 -> SetupFinishedPage {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            GraspDatabase.getInstance()?.configDao()?.onSetupCompleted()
                        }
                    }
                    page++
                }
                4 -> Box(Modifier.fillMaxSize())
            }
        }
    }
}

