package com.example.medicalinventryapp.ui.screens.authentication

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicalinventryapp.R
import com.example.medicalinventryapp.navigation.authroutes
import com.example.medicalinventryapp.viewModels.MainViewModel
import kotlinx.coroutines.delay@Composable
fun LoginScreenUI(viewModel: MainViewModel, navController: NavController) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current
    val loginstate = viewModel.loginuserstate.collectAsState()
    val userid = viewModel.getuseridstate.collectAsState().value

    // Navigate only once when login is successful and userId is valid
    LaunchedEffect(loginstate.value.success, userid.userId) {
        if (loginstate.value.success != null && !userid.userId.isNullOrEmpty() && userid.userId != "null") {
            Log.d("LoginScreenUI", "Navigating to WaitingScreen with userId: ${userid.userId}")
            navController.navigate(authroutes.WaitingScreen(userid.userId)) {
                // Clear the entire back stack up to the start destination
                popUpTo(authroutes.Login) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        } else if (loginstate.value.error != null) {
            Log.e("LoginScreenUI", "Invalid userid: ${userid.userId}")
            Toast.makeText(context, "User ID not found, please try again", Toast.LENGTH_SHORT).show()
        }
    }

    when {
        loginstate.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        loginstate.value.error != null -> {
            Log.e("LoginScreenUI", "Login error: ${loginstate.value.error}")
            Toast.makeText(context, loginstate.value.error.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    Box {
        Image(
            painter = painterResource(id = R.drawable.img_1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().alpha(0.4f)
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Login",
                fontSize = 72.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Cursive,
                color = Color(219, 122, 83)
            )
            Spacer(modifier = Modifier.height(24.dp))
            InputField("Email", email.value, { email.value = it }, Icons.Default.Email)
            InputField("Password", password.value, { password.value = it }, Icons.Default.Lock)
            Spacer(modifier = Modifier.height(28.dp))
            Button(
                onClick = {
                    if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
                        viewModel.LoginUser(email.value, password.value)
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(219, 122, 83))
            ) {
                Text("Login", fontSize = 18.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(
                onClick = { navController.navigate(authroutes.SignUp) }
            ) {
                Text(text = "Don't have an account? SignUp", color = Color(174, 245, 61))
            }
        }
    }
}