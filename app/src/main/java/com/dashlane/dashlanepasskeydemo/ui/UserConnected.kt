package com.dashlane.dashlanepasskeydemo.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dashlane.dashlanepasskeydemo.ui.theme.BorderButtonColor
import com.dashlane.dashlanepasskeydemo.ui.theme.SecondaryBackground
import com.dashlane.dashlanepasskeydemo.ui.theme.SecondaryTextColor
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
@Preview
fun UserConnected(
    email: String = "test@dashlane.com",
    creationDate: Instant = Instant.now(),
    onDisconnect: () -> Unit = {},
    onSourceCodeOpen: () -> Unit = {},
) {
    val formattedCreationDate = DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneId.systemDefault()).format(creationDate)
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1F)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                text = "You’re logged in! \uD83C\uDF89",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = SecondaryBackground,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp),
            ) {
                Text(text = "Creation date", style = MaterialTheme.typography.titleSmall, color = SecondaryTextColor)
                Text(
                    text = formattedCreationDate,
                    style = MaterialTheme.typography.bodyLarge
                )
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(text = "Email", style = MaterialTheme.typography.titleSmall, color = SecondaryTextColor)
                Text(text = email, style = MaterialTheme.typography.bodyLarge)
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(text = "Contribute to this application", style = MaterialTheme.typography.titleSmall, color = SecondaryTextColor)
                Button(
                    onClick = { onSourceCodeOpen.invoke() },
                    border = BorderStroke(1.dp, BorderButtonColor),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                ) {
                    Text(text = "Open the source code")
                }
            }
            Button(
                onClick = { onDisconnect() },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                Text("Disconnect")
            }
        }
        BuildByDashlane()
    }
}