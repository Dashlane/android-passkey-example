package com.dashlane.dashlanepasskeydemo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BuildByDashlane() {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Made by Dashlane", modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp, top = 8.dp)
        )
    }
}