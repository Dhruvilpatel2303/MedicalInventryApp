package com.example.medicalinventryapp.ui.screens.dashboard.ProductsScreen

import android.R
import android.graphics.Paint
import android.graphics.fonts.Font
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicalinventryapp.navigation.authroutes
import com.example.medicalinventryapp.network.Response.productresponses.getAllrpoductsResponse.GetAllProductsResponseItem
import com.example.medicalinventryapp.viewModels.MainViewModel
import com.google.accompanist.placeholder.material3.placeholder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProductScreenUI(viewModel: MainViewModel, navController: NavController) {
    val allProductState = viewModel.getallproductstate.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
    }

    SideEffect {
        Log.d("TAG", "Recomposition happened")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(top=25.dp),
                modifier = Modifier.clip(CircleShape),
                title = {
                    Text(
                        text = "All Products",
                        color = Color.Yellow.copy(alpha = 0.7f),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(start = 30.dp)
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color.Red,
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .size(35.dp)
                            .clickable {
                                viewModel.getAllProducts()
                            }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF925F35),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF1D1915),
        contentColor = Color.White
    ){ paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        start = 6.dp,
                        end = 6.dp,
                        bottom =10.dp
                    )
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                when {
                    allProductState.isLoading -> {
                        LazyColumn(
                            modifier = Modifier
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF172123), // Dark gray
                                            Color(0xFFCDDEC5)  // Near black
                                        )
                                    )
                                ),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(10) { // Show 10 shimmer placeholders
                                ShimmerProductCard()
                            }
                        }
                    }
                    allProductState.error != null -> {
                        Text(text = allProductState.error ?: "Unknown Error", color = Color.Red)
                    }
                    allProductState.success != null -> {
                        LazyColumn(
                            modifier = Modifier
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0x6F2B3234), // Dark gray
                                            Color(0xFF243813)  // Near black
                                        )
                                    )
                                ).padding(bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(allProductState.success) { product ->
                                ProductCard(product, viewModel, onLongClick = {
                                    navController.navigate(authroutes.ProductDetailsScreen(product.product_id))
                                })
                            }
                        }
                    }
                }
            }
        }

}

// The ProductCard and ShimmerProductCard composables remain unchanged
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductCard(product: GetAllProductsResponseItem, viewModel: MainViewModel, onLongClick: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .combinedClickable(
                onClick = { isExpanded = !isExpanded },
                onLongClick = { onLongClick() }
            ),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C2E),
            contentColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color(0xFF64FFDA),
                    modifier = Modifier
                        .size(56.dp)
                        .padding(end = 16.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.product_name,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                        color = Color(0xFF64FFDA)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Category: ${product.product_category}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                        color = Color(0xFFB0BEC5)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "💰 Price: ₹${product.product_price}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand Icon",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Divider(color = Color.DarkGray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "🆔 Product ID ${product.product_id}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                    )

                    Text(
                        text = "📦 Quantity: ${product.product_quantity}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                    )
                }
            }
        }
    }
}

@Composable
fun ShimmerProductCard() {
    val shimmerColors = listOf(
        Color(69, 58, 50).copy(alpha = 0.2f),
        Color(73, 82, 60).copy(alpha = 0.7f),
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 100f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            )
        ), label = "shimmer"
    )

    val brush = Brush.radialGradient(
        colors = shimmerColors,
        center = Offset(translateAnim.value, translateAnim.value),
        radius = 10000f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C2E),
            contentColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(brush, shape = RoundedCornerShape(10.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(20.dp)
                            .background(brush, shape = RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(16.dp)
                            .background(brush, shape = RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(16.dp)
                            .background(brush, shape = RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}