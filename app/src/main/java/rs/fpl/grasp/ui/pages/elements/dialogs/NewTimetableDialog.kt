package rs.fpl.grasp.ui.pages.elements.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import rs.fpl.grasp.R
import rs.fpl.grasp.background.database.types.TimetableEntity

@Preview
@Composable
fun NewTimetableDialog(
    entity: TimetableEntity? = null,
    onCloseRequest: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    val isPreview = LocalInspectionMode.current
    IconDialog(
        iconPainter = painterResource(R.drawable.question_mark_24px),
        title = "is this correct?",
        cancelButtonText = "no",
        onCancel = onCloseRequest,
        confirmButtonText = "yes",
        onConfirm = onConfirm,
        content = {
            TimetableInfo(entity.takeUnless { isPreview })
        },
    )
}
@Composable
fun TimetableInfo(entity: TimetableEntity?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KVPString("Id", entity?.id ?: "<id>")
        KVPString("Name", entity?.timetable?.general?.title ?: "<title>")
        KVPString("Year", entity?.timetable?.general?.year ?: "<year>")
        KVPString("Date", entity?.timetable?.general?.date ?: "<date>")
        KVPString("School", entity?.timetable?.general?.schoolName ?: "<schoolName>")
    }
}
@Composable
private fun KVPString(key: String, value: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))) {
                append("$key: ")
            }
            append(value)
        },
        textAlign = TextAlign.Center
    )
}