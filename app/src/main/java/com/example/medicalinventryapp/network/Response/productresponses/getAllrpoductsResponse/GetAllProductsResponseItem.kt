package com.example.medicalinventryapp.network.Response.productresponses.getAllrpoductsResponse

data class GetAllProductsResponseItem(
    val id: Int,
    val product_category: String,
    val product_id: String,
    val product_name: String,
    val product_price: String, // Changed to String
    val product_quantity: String // Changed to String
)