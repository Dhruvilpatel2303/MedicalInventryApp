package com.example.medicalinventryapp.ui.screens.dashboard.ProductsScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicalinventryapp.viewModels.MainViewModel

@Composable
fun ProductDetailsScreenUI(
        product_Id: String,
        viewModel: MainViewModel,
        navController: NavController
    ) {
        val productstate = viewModel.getspecificproductstate.collectAsState().value

        // Fetch product by ID when the screen launches
        LaunchedEffect(Unit) {
            Log.d("DETAIL_SCREEN", "Product ID: $product_Id")
            viewModel.getSpecificProductResponse(product_Id)

        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1C1C1E))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                productstate.isLoading -> {
                    CircularProgressIndicator(color = Color(0xFF64FFDA))
                }

                productstate.error != null -> {
                    Text(
                        text = productstate.error ?: "Something went wrong!",
                        color = Color.Red,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                productstate.success != null -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .align(Alignment.TopCenter),
                        elevation = CardDefaults.cardElevation(12.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2C2C2E),
                            contentColor = Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(
                                text = "Product Details",
                                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 24.sp),
                                color = Color(0xFF64FFDA),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            DetailRow(label = "Product Name", value = productstate.success.product_name)
                            DetailRow(label = "Product ID", value = productstate.success.product_id)
                            DetailRow(label = "Category", value = productstate.success.product_category)
                            DetailRow(label = "Price", value = "₹${productstate.success.product_price}")
                            DetailRow(label = "Quantity", value = productstate.success.product_quantity.toString())
                        }
                    }
                }
            }
        }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
        Divider(color = Color.DarkGray, thickness = 1.dp, modifier = Modifier.padding(top = 8.dp))
    }
}