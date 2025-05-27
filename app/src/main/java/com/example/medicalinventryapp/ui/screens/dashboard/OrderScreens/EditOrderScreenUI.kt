package com.example.medicalinventryapp.ui.screens.dashboard.OrderScreens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicalinventryapp.network.Response.orderResponse.getSpecificOrderResponse.GetSpecificOrderResponse
import com.example.medicalinventryapp.network.Response.orderResponse.getallorders.GetallOrdersResponseItem
import com.example.medicalinventryapp.network.Response.productresponses.getSpecificProductResponse.GetSpeciificProductResponse
import com.example.medicalinventryapp.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun EditOrderScreenUI(
    order_id: String,
    viewModel: MainViewModel,
    navController: NavController
) {
    val getSpecificOrderState = viewModel.getspecificorderstate.collectAsState().value
    val updateOrderState = viewModel.updateorderstate.collectAsState().value
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Fetch order details when the screen is composed
    LaunchedEffect(order_id) {
        viewModel.getSpecificOrderDetails(order_id)
    }

//    // Handle update success
//    LaunchedEffect(updateOrderState.success) {
//
//            navController.popBackStack()
//        // Navigate back on successful update
//
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Order",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD9B514)
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .clickable { navController.popBackStack() }
                            .padding(start = 16.dp)
                            .size(28.dp),
                        tint = Color(0xFFCEA819)
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                getSpecificOrderState.isLoading -> {
                    CircularProgressIndicator(
                        color = Color(0xFFA7230D),
                        trackColor = Color(0xFF3F7B6C),
                        strokeWidth = 7.dp,
                        modifier = Modifier.size(100.dp)
                    )
                }
                getSpecificOrderState.error != null -> {
                    Text(
                        text = "Error: ${getSpecificOrderState.error}",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                getSpecificOrderState.success != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        EditOrderCard(
                            order = getSpecificOrderState.success,
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }

            // Show update error if present
            if (updateOrderState.error != null) {
                Text(
                    text = "Update Error: ${updateOrderState.error}",
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                )
            }

            // Show update loading indicator
            if (updateOrderState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp),
                    color = Color(0xFF85C72B)
                )
            }
        }
    }
}
@Composable
fun EditOrderCard(
    order: GetSpecificOrderResponse,
    viewModel: MainViewModel,
    navController: NavController
) {
    var quantity by remember { mutableStateOf(order.order_quantity?.toString() ?: "") }
    var message by remember { mutableStateOf(order.messaage ?: "") }
    var totalAmount by remember { mutableStateOf("0") }

    // Update totalAmount when quantity changes
    LaunchedEffect(quantity) {
        totalAmount = try {
            val qty = quantity.toIntOrNull() ?: 0
            val price = order.order_price?.toInt() ?: 1 // Ensure order.order_price is String?
            (qty * price).toString()
        } catch (e: Exception) { // Broader exception handling
            "0"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF644439), Color(0xFF1F1F3F))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "🆔 Order ID: ${order.order_id}",
            color = Color.White,
            fontSize = 16.sp
        )
        Text(
            text = "📦 Product: ${order.orderd_product_name}",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "💰 Price: ₹${order.order_price ?: "0"}",
            color = Color.Green,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quantity Input Field
        TextField(
            value = quantity,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                    quantity = newValue
                }
            },
            label = { Text("Quantity") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Message Input Field
        TextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Message") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Total Amount Display Field
        TextField(
            value = totalAmount,
            onValueChange = { }, // Read-only
            label = { Text("Total Amount (₹)") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            enabled = false,
            colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledLabelColor = Color.Gray,
                disabledContainerColor = Color(0xFFF0F0F0),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Update Button
        Button(
            onClick = {
                val qty = quantity.toIntOrNull() ?: 0
                viewModel.updateOrderDetails(
                    order_id = order.order_id,
                    order_quantity = qty.toString(),
                    message = message,
                    total_amount = totalAmount
                )
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8A5321)
            )
        ) {
            Text(
                text = "Update Order",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

        }
    }
}