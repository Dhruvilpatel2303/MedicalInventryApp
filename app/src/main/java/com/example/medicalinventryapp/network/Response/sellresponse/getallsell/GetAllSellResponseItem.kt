package com.example.medicalinventryapp.network.Response.sellresponse.getallsell

data class GetAllSellResponseItem(
    val date_of_sell: String,
    val id: Int,
    val price: Int,
    val product_id: String,
    val product_name: String,
    val quantity: Int,
    val remaining_stock: Int,
    val sell_id: String,
    val total_amount: Int,
    val user_id: String,
    val user_name: String
)