package com.example.medicalinventryapp.ui.screens.dashboard.OrderScreens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicalinventryapp.navigation.authroutes
import com.example.medicalinventryapp.network.Response.orderResponse.getallorders.GetallOrdersResponseItem
import com.example.medicalinventryapp.viewModels.MainViewModel

import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreenUI(viewModel: MainViewModel, navController: NavController) {
    val getallordersstate = viewModel.getallordersstate.collectAsState().value
    val userId = viewModel.getuseridstate.collectAsState().value
    val filteredOrders = getallordersstate.success?.filter { it.user_id == userId.userId }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    LaunchedEffect(Unit) {
        viewModel.getAllOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(top=30.dp),
                modifier = Modifier.clip(CircleShape),
                navigationIcon = {
                    Icon(
                        Icons.Default.List,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { navController.navigate(authroutes.CreateOrderScreen) }
                            .size(50.dp)
                            .padding(start = 20.dp),
                        tint = Color(0xFFCEA819)
                    )
                },
                title = {
                    Text(
                        text = "Your Orders",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFD9B514),
                        modifier = Modifier.padding(start = 10.dp)
                    )
                },
                actions = {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { viewModel.getAllOrders() }
                            .padding(end = 16.dp)
                            .size(28.dp),
                        tint = Color(0xFFCEA819)
                    )
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { navController.navigate(authroutes.CreateOrderScreen) }
                            .padding(end = 16.dp),
                        tint = Color.White
                    )
                }, scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF754C2B)

                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(authroutes.CreateOrderScreen)
                },
                containerColor = Color(0xFF8A5321),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(70),
                modifier = Modifier
                    .padding(bottom = 50.dp,).width(85.dp).height(70.dp)

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Order",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(18.dp))

                }
            }
        },
        // Position FAB at the bottom-right
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                // Add padding to content to avoid bottom bar overlap
                .padding(bottom=30.dp)
        ) {
            when {
                getallordersstate.isLoading ->{
                    LazyColumn {
                        items(10) {
                            SimpleShimmerOrderCard()
                        }
                    }
                }
                getallordersstate.error != null -> {
                    Text("Error: ${getallordersstate.error}", color = Color.Red)
                }
                filteredOrders != null -> {
                    LazyColumn {
                        items(filteredOrders) { order ->
                            OrderDetails(order,navController,onDeleteClick={
                                viewModel.cancelOrder(order.order_id!!)
                                viewModel.getAllOrders()
                            },onEditClick={
                                navController.navigate(authroutes.EditOrderScreen(order.order_id!!))
                            })
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun OrderDetails(order: GetallOrdersResponseItem, navController: NavController,onDeleteClick: () -> Unit,onEditClick:() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)

            .background(Color(0xFF1C1C1E), RoundedCornerShape(12.dp))

    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF644439), Color(0xFF1F1F3F))
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Order Information
            Text(
                text = "🆔 Order ID: ${order.order_id}",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "👤 User: ${order.user_name}",
                color = Color.White
            )
            Text(
                text = "📦 Product: ${order.orderd_product_name}",
                color = Color.White
            )
            Text(
                text = "📅 Date: ${order.date_of_order_creation}",
                color = Color.Gray
            )

            // Pricing and Quantity
            Text(
                text = "💰 Price: ₹${order.order_price}",
                color = Color.Green
            )
            Text(
                text = "🔢 Quantity: ${order.order_quantity}",
                color = Color.Yellow
            )
            Text(
                text = "💵 Total: ₹${order.total_amount}",
                color = Color.Cyan,
                fontWeight = FontWeight.Bold
            )

            // Approval Status and Message
            Text(
                text = "✅ Approved: ${if (order.isApproved == 1) "Yes" else "No"}",
                color = if (order.isApproved == 1) Color.Green else Color.Red
            )
            Text(
                text = "💬 Message: ${order.messaage}",
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Button
            Button(
                onClick = {
                   onEditClick()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF764B23),
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Order")
            }

            Spacer(modifier = Modifier.height(16.dp))


            Button(onClick = {
                onDeleteClick()

            },modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF764B23),
                    contentColor = Color.White
                )
                ) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cancel Order")

            }
        }
    }
}





@Composable
fun SimpleShimmerOrderCard() {
    val transition = rememberInfiniteTransition()

    val translateAnim=transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            )
        )
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        ),
        start = Offset(translateAnim.value, translateAnim.value),
        end = Offset(translateAnim.value + 200f, translateAnim.value + 200f)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color(0xFF1C1C1E), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(shimmerBrush, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(16.dp)
                    .background(shimmerBrush, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(16.dp)
                    .background(shimmerBrush, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(shimmerBrush, RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(shimmerBrush, RoundedCornerShape(12.dp))
            )
        }
    }
}