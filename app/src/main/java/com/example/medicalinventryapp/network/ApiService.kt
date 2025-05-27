package com.example.medicalinventryapp.network

import com.example.medicalinventryapp.network.Response.orderResponse.cancelOrderResponse.CancelOrderResponse
import com.example.medicalinventryapp.network.Response.orderResponse.createorderResponse.CreateOrderResponse
import com.example.medicalinventryapp.network.Response.orderResponse.getSpecificOrderResponse.GetSpecificOrderResponse
import com.example.medicalinventryapp.network.Response.orderResponse.getallorders.GetallOrdersResponse
import com.example.medicalinventryapp.network.Response.orderResponse.updateOrderResponse.UpdateOrderResponse
import com.example.medicalinventryapp.network.Response.productresponses.getAllrpoductsResponse.GetAllProductsResponse
import com.example.medicalinventryapp.network.Response.productresponses.getSpecificProductResponse.GetSpeciificProductResponse
import com.example.medicalinventryapp.network.Response.sellresponse.createSellOrder.CreateSellOrderResponse
import com.example.medicalinventryapp.network.Response.sellresponse.getallsell.GetAllSellResponse
import com.example.medicalinventryapp.network.Response.sellresponse.getallsell.GetAllSellResponseItem
import com.example.medicalinventryapp.network.Response.usersResponse.CreateUserResponse
import com.example.medicalinventryapp.network.Response.usersResponse.DeleteUserResponse
import com.example.medicalinventryapp.network.Response.usersResponse.LoginUserResponse
import com.example.medicalinventryapp.network.Response.usersResponse.UpdateUserDetailsResponse
import com.example.medicalinventryapp.network.Response.usersResponse.WaitingUserResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST

interface  ApiService {


//    https://dhruvil2021.pythonanywhere.com/createuser
    @FormUrlEncoded
    @POST("createuser")
    suspend fun createUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phonenumber") phonenumber: String,
        @Field("address") address: String,
        @Field("pincode") pincode: String,

    ): Response<CreateUserResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun LoginUser(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<LoginUserResponse>



    @FormUrlEncoded
    @POST("getspecificuser")
    suspend fun getSpecificUserDetails(
        @Field("user_Id") user_id: String
    ): Response<WaitingUserResponse>

    @GET("getallproducts")
    suspend fun getAllproducts(): Response<GetAllProductsResponse>


    @FormUrlEncoded
    @POST("getspecificproduct")
    suspend fun getSpecificProductDetails(
        @Field("product_Id") user_id: String
    ): Response<GetSpeciificProductResponse>


    @FormUrlEncoded
    @POST("createorder")
    suspend fun createOrder(
        @Field("user_id") user_id:String,
        @Field("product_id") product_id:String,
        @Field("isApproved") isApproved:Int,
        @Field("order_quantity") quantity:String,
        @Field("order_price") price:String,
        @Field("total_amount") amount:String,
        @Field("ordered_product_name") product_name:String,
        @Field("user_name") user_name:String,
        @Field("message") message:String,
        @Field("category") category:String
    ): Response<CreateOrderResponse>


    @GET("getallorders")
    suspend fun getAllOrders(): Response<GetallOrdersResponse>


    @FormUrlEncoded
    @PATCH("updateordersir")
    suspend fun editOrderDetails(
        @Field("order_Id") order_id:String,
        @Field("order_quantity") order_quantity: String,
        @Field("message") message: String,
        @Field("total_amount") total_amount: String
    ): Response<UpdateOrderResponse>



    @FormUrlEncoded
    @POST("getspecificorder")
    suspend fun getSpecificOrderDetails(
        @Field("order_Id") order_id: String
    ): Response<GetSpecificOrderResponse>


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "deleteorder", hasBody = true)
    suspend fun DeleteOrder(
        @Field("order_Id") order_id: String

    ): Response<CancelOrderResponse>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "deleteuser", hasBody = true)
    suspend fun deleteUser(
        @Field("user_Id") user_id: String
    ): Response<DeleteUserResponse>



    @GET("getallsells")
    suspend fun getsellhistory(

    ):Response<GetAllSellResponse>


    @FormUrlEncoded
    @POST("createsellorder")
    suspend fun createSellOrder(
        @Field("user_id") user_id:String,
        @Field("product_id") product_id:String,
        @Field("product_name") product_name:String,
        @Field("user_name") user_name:String,
        @Field("remaining_stock") remaining_stock:String,
        @Field("total_amount") total_amount:String,
        @Field("price") price: String,
        @Field("quantity") quantity: String


    ): Response<CreateSellOrderResponse>

    @FormUrlEncoded
    @PATCH("updateusersir")
    suspend fun edituserdetails(
        @Field("user_Id") user_id:String,
        @Field("name") user_name:String,
        @Field("email") email:String,
        @Field("password") password:String,
        @Field("phone_number") phonenumber:String,
        @Field("address") address:String,
        @Field("pincode") pincode:String,

    ): Response<UpdateUserDetailsResponse>



}