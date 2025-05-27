package com.example.medicalinventryapp.network.Response.productresponses.getSpecificProductResponse

data class GetSpeciificProductResponse(
    val id: Int,
    val product_category: String,
    val product_id: String,
    val product_name: String,
    val product_price: Double,
    val product_quantity: Int
)