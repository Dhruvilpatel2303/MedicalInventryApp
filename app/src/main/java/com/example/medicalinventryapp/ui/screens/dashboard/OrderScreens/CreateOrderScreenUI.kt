package com.example.medicalinventryapp.ui.screens.dashboard.OrderScreens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicalinventryapp.navigation.authroutes
import com.example.medicalinventryapp.network.Response.productresponses.getAllrpoductsResponse.GetAllProductsResponseItem
import com.example.medicalinventryapp.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderScreenUI(viewModel: MainViewModel,navController: NavController) {
    val allProductState = viewModel.getallproductstate.collectAsState().value
    val userid=viewModel.getuseridstate.collectAsState().value
    val getuserdeatilstate=viewModel.getspecificuser.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
        viewModel.getSpecificUserDetails(userid.userId.toString())
    }


    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(top = 35.dp),
                modifier = Modifier.clip(CircleShape),
                title = {
                    Text(
                        "Place Order",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFD9B514),
                        modifier = Modifier.padding(start = 90.dp)
                    )
                },
                navigationIcon = {
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        contentDescription = "back button",
                        modifier = Modifier
                            .clickable {
                                navController.navigate(authroutes.OrderScreen)
                            }
                            .padding(start = 10.dp)
                            .size(40.dp),
                        tint = Color(0xFFD9B514)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3E4619)

                )
            )
        },
        containerColor =Color(0xFF112608)





    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp, start = 6.dp, end = 6.dp)

        ) {

            Spacer(modifier = Modifier.height(8.dp))

            when {
                allProductState.isLoading -> {
//                    Box(
////                        modifier = Modifier.fillMaxSize(),
////                        contentAlignment = Alignment.Center
////                    ) {
////                        CircularProgressIndicator(
////                            color = Color(0xFF85C72B),
////                            trackColor = Color(0xFF3F7B6C),
////                            strokeCap = StrokeCap.Square,
////                            strokeWidth = 7.dp,
////                            modifier = Modifier.size(100.dp) // ⬅️ Set size here
////                        )
////                    }

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(10) { // Show 5 shimmer placeholders
                            ShimmerOrderCard()
                        }
                    }

                }

                allProductState.error != null -> {
                    Text(text = allProductState.error ?: "Unknown Error", color = Color.Red)
                }

                allProductState.success != null -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(allProductState.success) { product ->
                            OrderCard(product, viewModel, onLongClick = {
                                navController.navigate(authroutes.MakeOrderScreen(product.product_id))



                            }, username =getuserdeatilstate.success?.name, user_id = userid.userId)
                        }
                    }
                }
            }
        }

    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrderCard(
    product: GetAllProductsResponseItem,
    viewModel: MainViewModel,
    onLongClick: () -> Unit,
    username: String?,
    user_id: String?
) {
    val context= LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    val createOrder = remember { mutableStateOf(false) }

    val product_price = product.product_price?.toString() ?: "0"
    val product_id = product.product_id?.toString() ?: "N/A"
    val product_name = product.product_name?.toString() ?: "N/A"
    val product_category = product.product_category?.toString() ?: "N/A"
    val product_quantity_available = product.product_quantity?.toString() ?: "0"

    val productQuantity = remember { mutableStateOf("") }
    val isApproved = remember { mutableStateOf("0") }
    val message = remember { mutableStateOf("") }
    val user_name = remember { mutableStateOf("") }

    // Calculate total amount dynamically
    val totalAmount = remember(productQuantity.value) {
        val qty = productQuantity.value.toFloatOrNull()?.coerceAtLeast(0f) ?: 0f
        val price = product_price.toFloatOrNull() ?: 0f
        (qty * price).toInt()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = { isExpanded = !isExpanded },
                onLongClick = { onLongClick() }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black,
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF644439), Color(0xFF1F1F3F))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color(0xFFFF6F61),
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 12.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product_name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFFF0C562)
                    )
                    Text(
                        text = "Category: $product_category",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFB0BEC5)
                    )
                    Text(
                        text = "💰 Price: ₹$product_price",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "📦 Quantity Available: $product_quantity_available",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300)),
                exit = slideOutVertically(
                    targetOffsetY = { it / 2 },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider(color = Color(0xFF616161), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "🆔 Product ID: $product_id",
                        fontSize = 14.sp,
                        color = Color(0xFFB0BEC5)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { createOrder.value = !createOrder.value },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6F61),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Create Order", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            AnimatedVisibility(
                visible = isExpanded && createOrder.value,
                enter = slideInHorizontally(
                    initialOffsetX = { it / 3 },
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))+ expandHorizontally(
                    expandFrom = Alignment.End,
                    animationSpec = tween(400)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { it / 2 },
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))+ shrinkHorizontally(
                    shrinkTowards = Alignment.CenterHorizontally,

                    animationSpec = tween(400)

                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = product_name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Product Name", fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedBorderColor = Color(0xFF64B5F6),
                            unfocusedBorderColor = Color(0xFF42A5F5),
                            disabledBorderColor = Color(0xFF90CAF9),
                            focusedLabelColor = Color(0xFF64B5F6),
                            unfocusedLabelColor = Color(0xFF42A5F5),
                            disabledLabelColor = Color(0xFF90CAF9),
                            disabledTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = product_category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Product Category", fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
                        colors =OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedBorderColor = Color(0xFF81C784),
                            unfocusedBorderColor = Color(0xFF66BB6A),
                            disabledBorderColor = Color(0xFFA5D6A7),
                            focusedLabelColor = Color(0xFF81C784),
                            unfocusedLabelColor = Color(0xFF66BB6A),
                            disabledLabelColor = Color(0xFFA5D6A7),
                            disabledTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = product_price,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Product Price", fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedBorderColor = Color(0xFFFFB300),
                            unfocusedBorderColor = Color(0xFFFFA000),
                            disabledBorderColor = Color(0xFFFFCC80),
                            focusedLabelColor = Color(0xFFFFB300),
                            unfocusedLabelColor = Color(0xFFFFA000),
                            disabledLabelColor = Color(0xFFFFCC80),
                            disabledTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = productQuantity.value,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } || input.isEmpty()  ) {
                                if( input < product_quantity_available){
                                    productQuantity.value = input
                                }
                                else{
                                    Toast.makeText(context,"enter quantity less than avilabel",Toast.LENGTH_SHORT).show()

                                }

                            }
                        },
                        label = { Text("Product Quantity", fontSize = 14.sp) },
                        placeholder = { Text("Enter quantity", fontSize = 14.sp, color = Color(0xFFB0BEC5)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedBorderColor = Color(0xFFAB47BC),
                            unfocusedBorderColor = Color(0xFF8E24AA),
                            disabledBorderColor = Color(0xFFCE93D8),
                            focusedLabelColor = Color(0xFFAB47BC),
                            unfocusedLabelColor = Color(0xFF8E24AA),
                            disabledLabelColor = Color(0xFFCE93D8),
                            cursorColor = Color(0xFFAB47BC),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = username.toString(),
                        onValueChange = { user_name.value = it },
                        label = { Text("User Name", fontSize = 14.sp) },
                        placeholder = { Text("Enter your name", fontSize = 14.sp, color = Color(0xFFB0BEC5)) },
                        singleLine = true,
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedBorderColor = Color(0xFFEF5350),
                            unfocusedBorderColor = Color(0xFFE53935),
                            disabledBorderColor = Color(0xFFFFCDD2),
                            focusedLabelColor = Color(0xFFEF5350),
                            unfocusedLabelColor = Color(0xFFE53935),
                            disabledLabelColor = Color(0xFFFFCDD2),
                            cursorColor = Color(0xFFEF5350),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        value = "₹$totalAmount",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Total Amount", fontSize = 14.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedBorderColor = Color(0xFFEC407A),
                            unfocusedBorderColor = Color(0xFFE91E63),
                            disabledBorderColor = Color(0xFFF8BBD0),
                            focusedLabelColor = Color(0xFFEC407A),
                            unfocusedLabelColor = Color(0xFFE91E63),
                            disabledLabelColor = Color(0xFFF8BBD0),
                            disabledTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = message.value,
                        onValueChange = { message.value = it },
                        label = { Text("Message (Optional)", fontSize = 14.sp) },
                        placeholder = { Text("Add a message", fontSize = 14.sp, color = Color(0xFFB0BEC5)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedBorderColor = Color(0xFFFF8A65),
                            unfocusedBorderColor = Color(0xFFF4511E),
                            disabledBorderColor = Color(0xFFFFB300),
                            focusedLabelColor = Color(0xFFFF8A65),
                            unfocusedLabelColor = Color(0xFFF4511E),
                            disabledLabelColor = Color(0xFFFFB300),
                            cursorColor = Color(0xFFFF8A65),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Button(
                        onClick = {

                                viewModel.createOrder(
                                    user_id.toString(),
                                    product_id,
                                    isApproved.value.toInt(),
                                    productQuantity.value,
                                    product_price,
                                    totalAmount.toString(),
                                    product_name,
                                    user_name.value,
                                    message.value,
                                    product_category
                                )
                                // Reset form after submission
                                productQuantity.value = ""
                                user_name.value = ""
                                message.value = ""
                                createOrder.value = false

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        enabled = productQuantity.value.isNotEmpty()
                    ) {
                        Text("Place Order", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}



@Composable
fun ShimmerOrderCard() {
    // Shimmer Animation
    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.6f),
        Color.White.copy(alpha = 0.2f),
        Color.White.copy(alpha = 0.6f)
    )
    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .drawWithContent {
                drawContent()
                val brush = Brush.linearGradient(
                    colors = shimmerColors,
                    start = Offset(translateAnim.value - 200f, 0f),
                    end = Offset(translateAnim.value, 0f)
                )
                drawRect(brush = brush, alpha = 0.3f)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black,
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF644439), Color(0xFF1F1F3F))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(24.dp)
                            .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(16.dp)
                            .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(16.dp)
                            .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(16.dp )
                            .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    )
                }
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                )
            }
        }
    }
}