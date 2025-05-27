package com.example.medicalinventryapp.ui.screens.authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import com.example.medicalinventryapp.viewModels.MainViewModel
import com.example.medicalinventryapp.R
import com.example.medicalinventryapp.navigation.authroutes

@Composable
fun SignUpScreenUI(viewModel: MainViewModel, navController: NavController) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val pincode = remember { mutableStateOf("") }
    val context = LocalContext.current
    val createUserState = viewModel.createuserstate.collectAsState()

    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val phoneFocus = remember { FocusRequester() }
    val pincodeFocus = remember { FocusRequester() }
    val addressFocus = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        viewModel.resetCreateUserState()
    }

    when {
        createUserState.value.success != null -> {
            navController.navigate(authroutes.Login)
        }

        createUserState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        createUserState.value.error != null -> {
            Toast.makeText(context, createUserState.value.error.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    Box {
        Image(
            painter = painterResource(id = R.drawable.img_1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().alpha(0.5f)
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Sign Up",
                fontSize = 72.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Cursive,
                color = Color(219, 122, 83)
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputField("Name", name.value, { name.value = it }, Icons.Default.Person, imeAction = ImeAction.Next) {
                emailFocus.requestFocus()
            }
            InputField("Email", email.value, { email.value = it }, Icons.Default.Email, emailFocus, ImeAction.Next) {
                passwordFocus.requestFocus()
            }
            InputField("Password", password.value, { password.value = it }, Icons.Default.Lock, passwordFocus, ImeAction.Next) {
                phoneFocus.requestFocus()
            }
            InputField("Phone Number", phoneNumber.value, { phoneNumber.value = it }, Icons.Default.Phone, phoneFocus, ImeAction.Next) {
                pincodeFocus.requestFocus()
            }
            InputField("Pincode", pincode.value, { pincode.value = it }, Icons.Default.LocationOn, pincodeFocus, ImeAction.Next) {
                addressFocus.requestFocus()
            }
            InputField("Address", address.value, { address.value = it }, Icons.Default.Home, addressFocus, ImeAction.Done) {}

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    if (name.value.isNotEmpty() && email.value.isNotEmpty() && password.value.isNotEmpty()
                        && phoneNumber.value.isNotEmpty() && address.value.isNotEmpty() && pincode.value.isNotEmpty()) {

                        if( email.value.endsWith("@gmail.com")) {
                            Toast.makeText(context, "Email must end with @gmail.com", Toast.LENGTH_SHORT).show()
                            viewModel.createUser(
                                email.value,
                                password.value,
                                name.value,
                                phoneNumber.value,
                                address.value,
                                pincode.value
                            )
                            Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                        }
                        Toast.makeText(context, "Email must end with @gmail.com", Toast.LENGTH_SHORT).show()



                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(219, 122, 83))
            ) {
                Text("Sign Up", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(onClick = {
                navController.navigate(authroutes.Login)
            }) {
                Text(text = "Already have an account? Login", color = Color(174, 245, 61))
            }
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector,
    focusRequester: FocusRequester = remember { FocusRequester() },
    imeAction: ImeAction = ImeAction.Next,
    onNext: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = "$label icon") },
        placeholder = { Text("Enter your $label") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 20.sp,
            color = Color(193, 245, 71)
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(onNext = { onNext() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(89, 105, 52),
            cursorColor = Color(219, 122, 83),
            focusedLabelColor = Color(219, 122, 40),
            unfocusedBorderColor = Color(219, 122, 83).copy(alpha = 0.68f),
            focusedTextColor = Color(193, 245, 71),
            unfocusedTextColor = Color(219, 122, 40),
            focusedContainerColor = Color.Black.copy(alpha = 0.5f),
            unfocusedContainerColor = Color.Black.copy(alpha = 0.3f)
        )
    )
}
