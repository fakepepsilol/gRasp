package rs.fpl.grasp.ui.pages.setup.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.fpl.grasp.R
import rs.fpl.grasp.background.GlobalRegex
import rs.fpl.grasp.ui.pages.elements.dialogs.NewTimetableDialog

@Preview
@Composable
fun AddATimetablePage(onceCompleted: () -> Unit = {}) {
    val isPreview = LocalInspectionMode.current
    val vm = if (isPreview) null else hiltViewModel<SetupPageViewModel>()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            val scope = rememberCoroutineScope()

            AppTitle()

            val urlTextState = rememberTextFieldState("")
            var isError by remember { mutableStateOf(false) }
            InputField(state = urlTextState, isError) { isError = false }

            var isLoading by remember { mutableStateOf(false) }
            var isDialogOpen by remember { mutableStateOf(false) }
            val softwareKeyboardController = LocalSoftwareKeyboardController.current
            NextButton(isLoading, onClick = {
                if (urlValid(urlTextState)) {
                    isLoading = true
                    softwareKeyboardController?.hide()
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            if (isPreview) {
                                delay(2000)
                            } else {
                                requireNotNull(vm).fetchInfo(urlTextState.text.toString())
                            }
                            isDialogOpen = true
                            isLoading = false
                        }
                    }
                } else {
                    isError = true
                }
            })
            if(isDialogOpen) {
                NewTimetableDialog(
                    vm?.timetableEntity,
                    onCloseRequest = {
                        isDialogOpen = false
                    },
                    onConfirm = {
                        isDialogOpen = false
                        vm?.saveTimetable()
                        onceCompleted()
                    }
                )
            }
        }
    }
}
private fun urlValid(state: TextFieldState): Boolean =
    state.text.matches(GlobalRegex.URL_REGEX_NAMED)


@Preview
@Composable
private fun AppTitle() {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)) {
                append("g")
            }
            withStyle(SpanStyle(fontSize = 30.sp, fontWeight = FontWeight.SemiBold)) {
                append("Rasp")
            }
        }, color = MaterialTheme.colorScheme.onBackground
    )
}

@Preview
@Composable
fun InputField(
    state: TextFieldState = TextFieldState(),
    isError: Boolean = false,
    onTextChanged: () -> Unit = {}
) {
    LaunchedEffect(state.text) {
        onTextChanged()
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp, 0.dp),
        state = state,
        label = @Composable {
            Text("Rasp URL")
        },
        lineLimits = TextFieldLineLimits.SingleLine,
        isError = isError,
    )
}

@Preview
@Composable
private fun NextButton(
    isLoading: Boolean = false, onClick: () -> Unit = {}
) {
    Button(
        modifier = Modifier
            .width(55.dp)
            .height(55.dp),
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                strokeWidth = 3.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                painter = painterResource(R.drawable.arrow_forward_24px),
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = "next button icon"
            )
        }
    }
}