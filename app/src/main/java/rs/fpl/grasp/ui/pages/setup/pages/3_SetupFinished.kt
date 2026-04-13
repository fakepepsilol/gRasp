package rs.fpl.grasp.ui.pages.setup.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.fpl.grasp.R

@Preview
@Composable
fun SetupFinishedPage(onceCompleted: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(120.dp, 120.dp),
                painter = painterResource(R.drawable.check_circle_48px),
                contentDescription = "Setup completed icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Setup completed")
                Text(
                    text = buildAnnotatedString {
                        append("Welcome to ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("gRasp")
                        }
                    },
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onceCompleted,
                modifier = Modifier.size(100.dp, 40.dp),
                contentPadding = PaddingValues.Zero
            ) {
                Text("Finish")
            }
        }
    }
}