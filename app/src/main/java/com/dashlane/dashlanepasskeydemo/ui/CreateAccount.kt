package com.dashlane.dashlanepasskeydemo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dashlane.dashlanepasskeydemo.R

@Composable
@Preview
fun CreateAccountPage(email: String = "test@dashlane.com", onCreatePasskey: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1F)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Create a passkey for $email", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Passkeys are a secure way to log in to your accounts without using a password. They use your device’s security features, such as biometrics, to verify your identity and securely log you in to your accounts.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { onCreatePasskey.invoke() },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.height(40.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_passkey),
                        contentDescription = "Passkey Icon",
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Text("Create a passkey", modifier = Modifier.padding(start = 10.dp))
                }
            }
        }
        BuildByDashlane()
    }
}