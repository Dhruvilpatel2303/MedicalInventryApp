package com.example.medicalinventryapp.ui.screens.dashboard.ProfileScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicalinventryapp.navigation.authroutes
import com.example.medicalinventryapp.network.Response.usersResponse.WaitingUserResponse
import com.example.medicalinventryapp.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    navController: NavController,
    viewModel: MainViewModel
) {
    val user = viewModel.getspecificuser.collectAsState().value
    val deletestate = viewModel.deleteuserstate.collectAsState().value
    val userId = viewModel.getuseridstate.collectAsState().value.userId

    LaunchedEffect(Unit) {
        viewModel.getSpecificUserDetails(userId.toString())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(top=35.dp),
                title = {
                    Text(
                        text = "Profile",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFD3D9C8)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3B66A2)
                )
                ,
                modifier = Modifier.clip(CircleShape)
            )
        },
        content = { paddingValues ->
            if (user == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF232A34))
                        .padding(
                            top = paddingValues.calculateTopPadding() + 16.dp,
                            start = 20.dp,
                            end = 20.dp
                        )
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            // Header
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF0C2136),
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = user.success?.name ?: "Not Provided",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF222222)
                                    )
                                    Text(
                                        text = user.success?.email ?: "",
                                        fontSize = 14.sp,
                                        color = Color(0xFF666666)
                                    )
                                }
                            }

                            Divider(
                                modifier = Modifier.padding(vertical = 16.dp),
                                color = Color(0xFFE0E0E0)
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                ProfileDetail("Phone", user.success?.phone_number ?: "-", Icons.Default.Phone)
                                ProfileDetail("Address", user.success?.address ?: "-", Icons.Default.Place)
                                ProfileDetail("Email", user.success?.email ?: "-", Icons.Default.Email)
                                ProfileDetail("Pin Code", user.success?.pin_code ?: "-")
                                ProfileDetail("Account Created", user.success?.date_of_account_created ?: "-")
                                ProfileDetail("Approval Status", if (user.success?.isApproved == 1) "Approved" else "Pending")
                                ProfileDetail("Blocked", if (user.success?.isBlocked == 1) "Yes" else "No")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Buttons
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = {
                                navController.navigate(authroutes.EditProfileScreen)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Edit Profile")
                        }

                        Button(
                            onClick = {
                                viewModel.signOut()
                                navController.navigate(authroutes.Login) {
                                    popUpTo(0)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                        ) {
                            Text("Sign Out")
                        }

                        Button(
                            onClick = {
                                viewModel.deleteAcount(user.success?.user_id.toString())
                                viewModel.signOut()
                                if (deletestate.success?.status == 200) {
                                    navController.navigate(authroutes.Login) {
                                        popUpTo(0)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Delete Account")
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ProfileDetail(label: String, value: String, icon: ImageVector? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = Color(0xFF64B5F6),
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp)
            )
        }
        Text(
            text = "$label:",
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = Color(0xFFCA1D1D),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 20.sp,
            color = Color(0xFF7DBB18),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}