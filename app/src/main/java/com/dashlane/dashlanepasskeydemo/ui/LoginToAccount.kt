package com.dashlane.dashlanepasskeydemo.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dashlane.dashlanepasskeydemo.R
import com.dashlane.dashlanepasskeydemo.ui.theme.TextColor

@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun LoginPage(onCreateAccount: (String) -> Unit = {}, onPasskeyLogin: () -> Unit = {}, emailError: Boolean = false) {
    val username = remember { mutableStateOf(TextFieldValue()) }
    Column(
        modifier = Modifier
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
            Text(text = "Hello", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(18.dp))
            Text(text = "Sign in or Sign up", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal))
            Spacer(modifier = Modifier.height(50.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Column {
                    OutlinedTextField(
                        label = { Text(text = "Email", color = TextColor) },
                        value = username.value,
                        onValueChange = { username.value = it },
                        isError = emailError,
                        supportingText = { if (emailError) Text(text = "Invalid input, please type an existing email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "emailIcon") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 18.dp)
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onCreateAccount(username.value.text) },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Continue")
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                Divider(modifier = Modifier.weight(1F))
                Text(text = "OR", modifier = Modifier.padding(horizontal = 8.dp))
                Divider(modifier = Modifier.weight(1F))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onPasskeyLogin() },
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, Color(0xFF79747E)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextColor),
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
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
                    Text("Sign in with a passkey", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
        BuildByDashlane()
    }
}