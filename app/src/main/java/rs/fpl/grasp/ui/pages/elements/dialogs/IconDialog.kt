package rs.fpl.grasp.ui.pages.elements.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun IconDialog(
    iconPainter: Painter,
    title: String,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    contentDividersVisible: Boolean = true,
    cancelButtonVisible: Boolean = true,
    cancelButtonText: String = "cancel",
    onCancel: () -> Unit,
    confirmButtonText: String = "confirm",
    onConfirm: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(vertical = 12.dp),
    contentItemSpacing: Dp = 12.dp,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel
    ) {
        Card(
            modifier = Modifier
                .width(400.dp)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(Modifier.height(6.dp))
                IconAndTitle(accentColor, iconPainter, title)
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if(contentDividersVisible) {
                        HorizontalDivider(
                            Modifier
                                .fillMaxWidth()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(contentPadding),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(contentItemSpacing),
                            content = { content() }
                        )
                    }
                    if(contentDividersVisible) {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Buttons(
                    cancelButtonVisible = cancelButtonVisible,
                    cancelText = cancelButtonText,
                    onCancel = onCancel,
                    accentColor = accentColor,
                    confirmText = confirmButtonText,
                    onConfirm = onConfirm
                )
            }
        }
    }
}

@Composable
private fun IconAndTitle(
    accentColor: Color,
    iconPainter: Painter,
    title: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(24.dp, 24.dp),
            painter = iconPainter,
            tint = accentColor,
            contentDescription = "$title icon"
        )
        Text(
            text = title, fontWeight = FontWeight.SemiBold, fontSize = 20.sp
        )
    }
}

@Composable
private fun Buttons(
    cancelButtonVisible: Boolean,
    cancelText: String = "cancel",
    onCancel: () -> Unit,
    accentColor: Color,
    confirmText: String = "confirm",
    onConfirm: () -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 0.dp, 8.dp, 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (cancelButtonVisible) {
                Button(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(cancelText)
                }
            }
            Button(
                onClick = onConfirm, modifier = Modifier.weight(1f).let {
                    if (cancelButtonVisible)
                        it
                    else
                        it.padding(horizontal = 40.dp)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor
                )
            ) {
                Text(confirmText)
            }
        }
    }
}