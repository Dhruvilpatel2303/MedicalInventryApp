package com.example.medicalinventryapp.ui.screens.waiting

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicalinventryapp.R
import com.example.medicalinventryapp.navigation.appnavigation.AppNavigation
import com.example.medicalinventryapp.navigation.authroutes
import com.example.medicalinventryapp.viewModels.MainViewModel


@Composable
fun WaitingScreenUI(userId: String, viewModel: MainViewModel, navController: NavController) {
    val userState = viewModel.getspecificuser.collectAsState()

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            Log.d("WaitingScreenUI", "Fetching user details for userId: $userId")
            viewModel.getSpecificUserDetails(userId)
        } else {
            Log.e("WaitingScreenUI", "Empty userId, navigating to Login")
            navController.navigate(authroutes.Login) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    BackHandler {
        navController.navigate(authroutes.Login) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.mipmap.img_2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().alpha(0.4f)
        )

        when {
            userState.value.isLoading -> {
                CircularProgressIndicator(
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center),
                    strokeCap = StrokeCap.Butt,
                    strokeWidth = 4.dp
                )
            }
            userState.value.error != null -> {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: ${userState.value.error}",
                        color = Color.Red,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (userId.isNotEmpty()) {
                                viewModel.getSpecificUserDetails(userId)
                            }
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }
            userState.value.success != null -> {
                LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                    item {
                        Text(
                            text = "Welcome to the App!",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier= Modifier.padding(top = 24.dp, bottom = 12.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (userState.value.success?.isApproved == 1) {
                                "You're Approved"
                            } else {
                                "You're on the Waiting Screen"
                            },
                            fontSize = if (userState.value.success?.isApproved == 1) 18.sp else 22.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 24.dp),
                            color = if (userState.value.success?.isApproved == 1) Color(194, 214, 135) else Color.White
                        )
                    }
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Black.copy(alpha = 0.5f),
                                contentColor = Color(199, 131, 64)
                            ),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                val user = userState.value.success
                                Text("Name: ${user?.name ?: "-"}", fontSize = 19.sp, modifier = Modifier.padding(vertical = 4.dp))
                                Text("Email: ${user?.email ?: "-"}", fontSize = 19.sp, modifier = Modifier.padding(vertical = 4.dp))
                                Text("Address: ${user?.address ?: "-"}", fontSize = 19.sp, modifier = Modifier.padding(vertical = 4.dp))
                                Text("Phone: ${user?.phone_number ?: "-"}", fontSize = 19.sp, modifier = Modifier.padding(vertical = 4.dp))
                                Text("Pin: ${user?.pin_code ?: "-"}", fontSize = 19.sp, modifier = Modifier.padding(vertical = 4.dp))
                                Text("Approved: ${if (user?.isApproved == 1) "Yes" else "No"}", fontSize = 19.sp, modifier = Modifier.padding(vertical = 4.dp))
                                Text("Created: ${user?.date_of_account_created ?: "-"}", fontSize = 19.sp, modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }
                    }
                    if (userState.value.success?.isApproved == 1) {
                        item {
                            Button(
                                onClick = {
                                    navController.navigate(authroutes.ProductsScreen) {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                        launchSingleTop = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color(245, 231, 223),
                                    containerColor = Color(68, 128, 84)
                                )
                            ) {
                                Text("Go To DashBoard ")
                                Icon(Icons.Default.ArrowForward, contentDescription = null)
                            }
                        }
                    }
                    item {
                        Button(
                            onClick = {
                                if (userId.isNotEmpty()) {
                                    viewModel.getSpecificUserDetails(userId)
                                }
                            }
                        ) {
                            Text("Refresh Screen")
                        }
                    }
                }
            }
            else -> {

                CircularProgressIndicator(
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center),
                    strokeCap = StrokeCap.Butt,
                    strokeWidth = 4.dp
                )
            }
        }
    }
}