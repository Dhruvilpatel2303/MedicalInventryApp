package com.example.medicalinventryapp.ui.screens.dashboard.ProfileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicalinventryapp.navigation.authroutes
import com.example.medicalinventryapp.viewModels.GetSpecificUser
import com.example.medicalinventryapp.viewModels.MainViewModel
import com.example.medicalinventryapp.viewModels.UpdateUserDetails
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreenUI(viewModel: MainViewModel, navController: NavController) {
    // State variables for form fields
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val userIdState by viewModel.getuseridstate.collectAsState()
    val userId = userIdState.userId

    val userDetailsState by viewModel.getspecificuser.collectAsState()

    LaunchedEffect(userId) {
        userId?.let { viewModel.getSpecificUserDetails(it) }
    }


    val updateUserDetailsState by viewModel.updateuserdetailsstate.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", fontSize = 30.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF623E18),
                    titleContentColor = Color.Black
                )
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            )
        },
        containerColor = Color(0xFF292B3D),
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                 ,
                singleLine = true,
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors (
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )


            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                   ,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors (
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )


            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                 ,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors (
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )


            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors (
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            // Address Field
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors (
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            OutlinedTextField(
                value = pinCode,
                onValueChange = { pinCode = it },
                label = { Text("Pin Code") },
                modifier = Modifier
                    .fillMaxWidth()
//                    .background(Color.White, shape = MaterialTheme.shapes.small),
                        ,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors (
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )


            Button(
                onClick = {
                    scope.launch {
                        if (userId == null) {
                            errorMessage = "User ID not available"
                            snackbarHostState.showSnackbar(errorMessage ?: "Unknown error")
                            return@launch
                        }
                        if (name.isBlank()) {
                            errorMessage = "Name cannot be empty"
                            snackbarHostState.showSnackbar(errorMessage ?: "Unknown error")
                            return@launch
                        }
                        if (email.isBlank() || !email.contains("@")) {
                            errorMessage = "Please enter a valid email"
                            snackbarHostState.showSnackbar(errorMessage ?: "Unknown error")
                            return@launch
                        }
                        if (password.isBlank()) {
                            errorMessage = "Password cannot be empty"
                            snackbarHostState.showSnackbar(errorMessage ?: "Unknown error")
                            return@launch
                        }
                        viewModel.updateuserdetails(
                            user_id = userId,
                            user_name = name,
                            email = email,
                            password = password,
                            phonenumber = phoneNumber,
                            address = address,
                            pincode = pinCode
                        )
                        navController.navigate(authroutes.ProfileScreen)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 16.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Save Changes",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}