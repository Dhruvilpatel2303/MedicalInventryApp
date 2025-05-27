package com.example.medicalinventryapp.ui.screens.dashboard.SellHistoryScreens

import android.R
import android.adservices.topics.Topic
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicalinventryapp.navigation.authroutes
import com.example.medicalinventryapp.network.Response.orderResponse.getallorders.GetallOrdersResponseItem
import com.example.medicalinventryapp.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSellOrderScreenUI(viewModel: MainViewModel, navController: NavController) {
    val userid = viewModel.getuseridstate.collectAsState().value.userId
    var userName = viewModel.getspecificuser.collectAsState().value.success?.name.toString()
    val getallordersstate = viewModel.getallordersstate.collectAsState().value
    val filteredOrders = getallordersstate.success?.filter { it.user_id == userid.toString() }

    // State to control the bottom sheet
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    // States for text fields
    var productId by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf(userid.toString()) }
    var selectedProductStock by remember { mutableStateOf(0) } // Store selected product's stock

    // Compute remaining stock dynamically
    val remainingStock by remember {
        derivedStateOf {
            val qty = quantity.toIntOrNull() ?: 0
            (selectedProductStock - qty).coerceAtLeast(0).toString()
        }
    }

    // Compute total amount dynamically
    val totalAmount by remember {
        derivedStateOf {
            val qty = quantity.toIntOrNull() ?: 0
            val prc = price.toDoubleOrNull() ?: 0.0
            String.format("%.2f", qty * prc)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getAllOrders()
        viewModel.getSpecificUserDetails(userId.toString())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(top = 35.dp),
                modifier = Modifier.clip(CircleShape),
                title = {
                    Text(
                        "Create Sell Order",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFD9B514),
                        modifier = Modifier.padding(start = 50.dp)
                    )
                },
                navigationIcon = {
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        contentDescription = "back button",
                        modifier = Modifier
                            .clickable {
                                navController.navigate(authroutes.SalesHistoryScreen)
                            }
                            .padding(start = 10.dp)
                            .size(40.dp),
                        tint = Color(0xFFD9B514)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF754C2B)

                )
            )
        },
        containerColor =Color(0xFF112608)

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFF112608))
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,

        ) {
            // Button to trigger the bottom sheet
            Button(
                onClick = { showBottomSheet = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Product")
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = productId,
                onValueChange = { productId = it },
                label = { Text("Product ID") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = quantity,
                onValueChange = { newValue ->
                    val qty = newValue.toIntOrNull() ?: 0
                    if (qty <= selectedProductStock) {
                        quantity = newValue
                    }
                },
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = totalAmount,
                onValueChange = { /* Read-only, computed dynamically */ },
                label = { Text("Total Amount") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("User ID") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("User Name") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = remainingStock,
                onValueChange = { /* Read-only, computed dynamically */ },
                label = { Text("Remaining Stock") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(26.dp))

            Button(
                onClick = {


                    if (quantity.toString().isNotEmpty()) {
                        viewModel.createSellOrder(
                            userid.toString(),
                            productId.toString(),
                            productName.toString(),
                            userName.toString(),
                            remainingStock.toString(),
                            totalAmount.toString(),
                            price.toString(),
                            quantity.toString(),




                            )
                        navController.navigate(authroutes.SalesHistoryScreen)
                    }




                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5D530D),
                    contentColor = Color.White
                )
            ) {
                Text("Make A Sell Order", fontWeight = FontWeight.ExtraBold)
            }

            // Bottom sheet
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    containerColor = Color(0xFF514928)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        Text(
                            text = "Orders",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp, start = 165.dp)
                        )

                        if (filteredOrders.isNullOrEmpty()) {
                            Text(
                                text = "No orders available",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            LazyColumn {
                                items(filteredOrders) { order ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                            .clickable {
                                                productId = order.product_id.toString()
                                                productName = order.orderd_product_name.toString()
                                                selectedProductStock =
                                                    order.order_quantity ?: 0 // Use remaining_stock
                                                price =
                                                    order.order_price.toString() // Set price from selected product
                                                quantity = "" // Reset quantity to avoid stale data
                                                showBottomSheet = false
                                            },
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp)
                                        ) {
                                            Text(
                                                text = "Product: ${order.orderd_product_name}",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Quantity: ${order.order_quantity}",
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Price: $${order.order_price}",
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))

                                        }
                                    }
                                    Divider()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}