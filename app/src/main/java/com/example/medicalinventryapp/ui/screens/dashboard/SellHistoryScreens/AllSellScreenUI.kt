package com.example.medicalinventryapp.ui.screens.dashboard.SellHistoryScreens

import android.R
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.medicalinventryapp.navigation.appnavigation.bottombar
import com.example.medicalinventryapp.navigation.authroutes
import com.example.medicalinventryapp.network.Response.sellresponse.getallsell.GetAllSellResponseItem
import com.example.medicalinventryapp.viewModels.MainViewModel

@Composable
fun AllSellScreenUI(viewModel: MainViewModel, navController: NavHostController) {
    // Define dark theme colors
    val darkColors = darkColors(
        primary = Color(0xFFBB86FC), // Purple for accents
        secondary = Color(0xFF03DAC6),
        background = Color(0xFF121212), // Dark gray background
        surface = Color(0xFF0F2B0C), // Darker blue for card background
        onSurface = Color.White,
        onBackground = Color.White,
        error = Color(0xFFCF6679)
    )

    val allsellorderstate = viewModel.getallsellstate.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getAllSellsHistory()
    }
    val userid = viewModel.getuseridstate.collectAsState().value.userId
    val allOrdersState =  allsellorderstate.value.success?.filter { it.user_id == userid.toString() }



    MaterialTheme(colors = darkColors) {
        Scaffold(
            topBar = {

                TopAppBar(
                    modifier = Modifier.clip(CircleShape),
                    windowInsets = WindowInsets(top = 35.dp),
                    title = {
                        Text(
                            text = "Sells History",
                            fontSize = 30.sp,
                            modifier = Modifier.padding( start = 20.dp),
                            fontWeight = FontWeight.ExtraBold,
                          color= Color(0xFFD9B514)

                        )
                    },
                    actions = {
                        IconButton(onClick = { viewModel.getAllSellsHistory() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                tint= Color(0xFFD9B514),
                                modifier = Modifier.padding(end = 30.dp).size(35.dp)
                            )
                        }
                    },
                   backgroundColor =Color(0xFF315422)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(authroutes.SellHistoryScreen)
                    },
                    backgroundColor = Color.White,
                    contentColor = Color.Black,
                    modifier = Modifier.padding(bottom = 100.dp, end = 10.dp).size(80.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Sale"
                    )
                }
            },

        ) { paddingValues ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                        .padding()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        allsellorderstate.value.isLoading -> {
                            CircularProgressIndicator(color = MaterialTheme.colors.primary)
                        }
                        allsellorderstate.value.success != null -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(allOrdersState!!) {
                                    SellItemCard(it)
                                }
                            }
                        }

                        allsellorderstate.value.error != null -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text =                         allsellorderstate.value.error ?: "An error occurred",
                                    style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                                    color = MaterialTheme.colors.error,
                                    modifier = Modifier.padding(16.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { viewModel.getAllSellsHistory() },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.primary,
                                        contentColor = MaterialTheme.colors.onSurface
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Retry", fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
            }

    }
}

@Composable
fun SellItemCard(item: GetAllSellResponseItem) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { isExpanded = !isExpanded }
            .animateContentSize()
            .border(1.dp, Color(0xFF112A82), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        backgroundColor = Color(0xFF1F2A39)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            // Product Name with Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = item.product_name,
                    style = MaterialTheme.typography.h6.copy(fontSize = 22.sp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Minimal View: Quantity and Total Amount
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Quantity",
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Quantity: ${item.quantity}",
                    style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Total Amount: $${String.format("%.2f", item.total_amount.toFloat())}",
                    style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }

            // Expanded View: Additional Details
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Sell ID",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Sell ID: ${item.sell_id}",
                        style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Date: ${item.date_of_sell}",
                        style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "User: ${item.user_name}",
                        style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Price: $${String.format("%.2f", item.price.toFloat())}",
                        style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Remaining Stock: ${item.remaining_stock}",
                        style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Product ID: ${item.product_id}",
                        style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User ID",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "User ID: ${item.user_id}",
                        style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}