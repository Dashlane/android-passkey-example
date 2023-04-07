package com.dashlane.dashlanepasskeydemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dashlane.dashlanepasskeydemo.ui.CreateAccountPage
import com.dashlane.dashlanepasskeydemo.ui.LoginPage
import com.dashlane.dashlanepasskeydemo.ui.UserConnected
import com.dashlane.dashlanepasskeydemo.ui.theme.DashlanePasskeyDemoTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DashlanePasskeyDemoTheme {
                ScreenMain()
            }
        }
    }

    @Composable
    fun ScreenMain(loginViewModel: LoginViewModel = hiltViewModel()) {
        val navController = rememberNavController()
        val uiState = loginViewModel.state.collectAsState(initial = LoginState.Initial).value
        LaunchedEffect(uiState) {
            when (uiState) {
                is LoginState.Initial -> {}
                is LoginState.EmailSuccess -> navController.navigate("create-account/${uiState.email}")
                is LoginState.EmailError -> {}
                is LoginState.CreateAccountSuccess -> {
                    Toast.makeText(this@MainActivity, "Account created", Toast.LENGTH_SHORT).show()
                    navController.navigate("login") { popUpTo(0) }
                }

                is LoginState.CreateAccountError -> {
                    Toast.makeText(this@MainActivity, "Account creation error -> " + uiState.message, Toast.LENGTH_SHORT).show()
                }

                is LoginState.LoginSuccess -> {
                    navController.navigate("connected/${uiState.email}/${uiState.creationDate}") { popUpTo(0) }
                }

                is LoginState.LoginError -> {
                    Toast.makeText(this@MainActivity, "Login error -> " + uiState.message, Toast.LENGTH_SHORT).show()
                }

                is LoginState.Disconnected -> navController.navigate("login") { popUpTo(0) }
            }
        }
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginPage(
                    onCreateAccount = { email -> loginViewModel.validateEmail(email, this@MainActivity) },
                    onPasskeyLogin = { loginViewModel.loginWithPasskey(this@MainActivity) },
                    emailError = uiState is LoginState.EmailError
                )
            }
            composable(
                "create-account/{email}",
                arguments = listOf(navArgument("email") { type = NavType.StringType })
            ) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email")!!
                CreateAccountPage(
                    email = email,
                    onCreatePasskey = {
                        loginViewModel.createPasskeyAccount(this@MainActivity, email)
                    }
                )
            }
            composable(
                "connected/{email}/{creationDate}",
                arguments = listOf(
                    navArgument("email") { type = NavType.StringType },
                    navArgument("creationDate") { type = NavType.LongType })
            ) { backStackEntry ->
                UserConnected(
                    email = backStackEntry.arguments?.getString("email")!!,
                    creationDate = backStackEntry.arguments?.getLong("creationDate")?.let { Instant.ofEpochSecond(it) }!!,
                    onDisconnect = { loginViewModel.disconnect() },
                    onSourceCodeOpen = { openSourceCodeWebPage() }
                )
            }
        }
    }

    private fun openSourceCodeWebPage() {
        // TODO : Replace with a real link to the source code
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.github.com"))
        startActivity(browserIntent)
    }
}

