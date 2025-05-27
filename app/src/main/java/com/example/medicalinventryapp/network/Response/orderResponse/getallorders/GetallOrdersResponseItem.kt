package com.example.medicalinventryapp.network.Response.orderResponse.getallorders

import kotlinx.serialization.Serializable


data class GetallOrdersResponseItem(
    val category: String? = null,
    val date_of_order_creation: String? = null,
    val id: Int? = null,
    val isApproved: Int = 0,
    val messaage: String? = null,
    val order_id: String? = null,
    val order_price: Int? = null,
    val order_quantity: Int? = null,
    val orderd_product_name: String? = null,
    val product_id: String? = null,
    val total_amount: Int? = null,
    val user_id: String? = null,
    val user_name: String? = null
)
