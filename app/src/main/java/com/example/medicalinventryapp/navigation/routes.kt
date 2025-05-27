package com.example.medicalinventryapp.navigation

import com.example.medicalinventryapp.network.Response.orderResponse.getallorders.GetallOrdersResponseItem
import kotlinx.serialization.Serializable

sealed class  authroutes{

    @Serializable
    object SignUp

    @Serializable
    object Login

    @Serializable
    data class WaitingScreen(
        val userID: String
    )






    @Serializable
    object ProductsScreen

    @Serializable
    data class ProductDetailsScreen(
        val product_Id: String
    )

    @Serializable
    object OrderScreen


    @Serializable
    object ProfileScreen

    @Serializable
    object SalesHistoryScreen

    @Serializable
    object DashBoard

    @Serializable
    object AllOrdersScreen

    @Serializable
    object CreateOrderScreen

    @Serializable
    data class MakeOrderScreen(
        val product_Id: String
    )


    @Serializable
    data class EditOrderScreen(
        val order_Id: String
    )

    @Serializable
    object startScreen

    @Serializable
    object SellHistoryScreen


    @Serializable
    object EditProfileScreen



}

