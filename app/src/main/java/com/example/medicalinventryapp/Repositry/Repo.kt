package com.example.medicalinventryapp.Repositry

import android.util.Log
import androidx.compose.ui.text.resolveDefaults
import com.example.medicalinventryapp.common.ResultState
import com.example.medicalinventryapp.network.ApiProvider
import com.example.medicalinventryapp.network.ApiProvider.provideApiService
import com.example.medicalinventryapp.network.Response.orderResponse.cancelOrderResponse.CancelOrderResponse
import com.example.medicalinventryapp.network.Response.orderResponse.createorderResponse.CreateOrderResponse
import com.example.medicalinventryapp.network.Response.orderResponse.getSpecificOrderResponse.GetSpecificOrderResponse
import com.example.medicalinventryapp.network.Response.orderResponse.getallorders.GetallOrdersResponse
import com.example.medicalinventryapp.network.Response.orderResponse.updateOrderResponse.UpdateOrderResponse
import com.example.medicalinventryapp.network.Response.productresponses.getAllrpoductsResponse.GetAllProductsResponse
import com.example.medicalinventryapp.network.Response.productresponses.getSpecificProductResponse.GetSpeciificProductResponse
import com.example.medicalinventryapp.network.Response.sellresponse.createSellOrder.CreateSellOrderResponse
import com.example.medicalinventryapp.network.Response.sellresponse.getallsell.GetAllSellResponse
import com.example.medicalinventryapp.network.Response.usersResponse.CreateUserResponse
import com.example.medicalinventryapp.network.Response.usersResponse.DeleteUserResponse
import com.example.medicalinventryapp.network.Response.usersResponse.LoginUserResponse
import com.example.medicalinventryapp.network.Response.usersResponse.UpdateUserDetailsResponse
import com.example.medicalinventryapp.network.Response.usersResponse.WaitingUserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import retrofit2.http.DELETE

class Repo {

    suspend fun createUser(
        name: String,
        email: String,
        password: String,
        phonenumber: String,
        address: String,
        pincode: String,
    ): Flow<ResultState<CreateUserResponse>> = flow {
        emit(ResultState.Loading)

        try {
            val response = ApiProvider.provideApiService()
                .createUser(name, email, password, phonenumber, address, pincode)

            if (response.isSuccessful) {
                emit(ResultState.Success(response.body()!!))
            } else {
                emit(ResultState.Error(Exception(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }


    suspend fun LoginUser(
        email: String,
        password: String,

        ): Flow<ResultState<LoginUserResponse>> = flow {

        emit(ResultState.Loading)

        try {
            val response = ApiProvider.provideApiService().LoginUser(email, password)
            if (response.isSuccessful) {
                emit(ResultState.Success(response.body()!!))
            } else {
                emit(ResultState.Error(Exception(response.errorBody()?.string())))
            }

        } catch (e: Exception) {
            emit(ResultState.Error(e))


        }
    }

    suspend fun WaitingUser(
        userId: String

    ): Flow<ResultState<WaitingUserResponse>> = flow {

        emit(ResultState.Loading)

        try {
            val response = ApiProvider.provideApiService().getSpecificUserDetails(userId)
            Log.d("WaitingUserAPI", "Raw Response: ${response.body()}")
            if (response.isSuccessful) {
                emit(ResultState.Success(response.body()!!))
            } else {
                emit(ResultState.Error(Exception(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }


    }

    //product functions


    suspend fun getAllProducts(): Flow<ResultState<GetAllProductsResponse>> = flow {

        emit(ResultState.Loading)
        try {
            Log.d("TAG2", "getAllProducts: ")
            val response = ApiProvider.provideApiService().getAllproducts()
            Log.d("TAG2", "getAllProducts: ${response.body()}")
            if (response.isSuccessful) {
                emit(ResultState.Success(response.body()!!))
            } else {
                emit(ResultState.Error(Exception(response.errorBody()?.string())))
            }


        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }

    suspend fun getSpecificProduct(productId: String): Flow<ResultState<GetSpeciificProductResponse>> =
        flow {

            emit(ResultState.Loading)

            try {
                val response = ApiProvider.provideApiService().getSpecificProductDetails(productId)
                if (response.isSuccessful) {
                    emit(ResultState.Success(response.body()!!))
                } else {
                    emit(ResultState.Error(Exception(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e))
            }
        }




    //order functions



    suspend fun CreateOrder(
        user_id: String,
        product_id: String,
        isApproved: Int,
        order_quantity: String,
        order_price: String,
        total_amount: String,
        product_name: String,
        user_name: String,
        message: String,
        category: String
    ): Flow<ResultState<CreateOrderResponse>> = flow {

        emit(ResultState.Loading)

        try {
            val response = ApiProvider.provideApiService().createOrder(
                user_id,
                product_id,
                isApproved,
                order_quantity,
                order_price,
                total_amount,
                product_name,
                user_name,
                message,
                category
            )

            if (response.isSuccessful) {
                emit(ResultState.Success(response.body()!!))
            } else {
                emit(ResultState.Error(Exception(response.errorBody()?.string())))
            }
        } catch (e: Exception) {

            emit(ResultState.Error(e))
        }




    }


    suspend fun getAllOrders(): Flow<ResultState<GetallOrdersResponse>> = flow {

        emit(ResultState.Loading)

        try {
            val response=ApiProvider.provideApiService().getAllOrders()

            if(response.isSuccessful){
                emit(ResultState.Success(response.body()!!))
            }
            else {
                emit(ResultState.Error(Exception(response.errorBody()?.string())))
            }
        }catch (E: Exception){
            emit(ResultState.Error(E))
        }
    }


    suspend fun updateOrderDetails(
        order_id: String,
        order_quantity : String,
        message: String,
        total_amount: String
    ):Flow<ResultState<UpdateOrderResponse>> =flow{


        emit(ResultState.Loading)

        try {
            val response= ApiProvider.provideApiService().editOrderDetails(order_id,order_quantity,message,total_amount)


            if (response.isSuccessful){
                emit(ResultState.Success<UpdateOrderResponse>(response.body()!!))
            }else{
                emit(ResultState.Error(Exception(response.errorBody()?.string())))
            }
        }catch (e: Exception){
            emit(ResultState.Error(e))
        }

    }

    suspend fun getSpecificOrderDetails(
        order_id: String
    ) : Flow<ResultState<GetSpecificOrderResponse>> = flow{

        emit(ResultState.Loading)

        try {
            val response= ApiProvider.provideApiService().getSpecificOrderDetails(order_id)

            if (response.isSuccessful){
                emit(ResultState.Success<GetSpecificOrderResponse>(response.body()!!))
            }else{
                emit(ResultState.Error(Exception(response.errorBody()?.string())))
            }
        }
        catch (e: Exception){
            emit(ResultState.Error(e))
        }






    }

    suspend fun CancelOrder(
        order_id: String

    ):Flow<ResultState<CancelOrderResponse>> = flow{
        emit(ResultState.Loading)

        try {
            val response= ApiProvider.provideApiService().DeleteOrder(order_id)

            if (response.isSuccessful){
                emit(ResultState.Success<CancelOrderResponse>(response.body()!!))
            }else{
                emit(ResultState.Error(Exception(response.errorBody()?.string())))
            }
        }
        catch (e: Exception){
            emit(ResultState.Error(e))
        }


    }

    suspend fun DeleteUser(
        user_id: String

    ): Flow<ResultState<DeleteUserResponse>> = flow{
        emit(ResultState.Loading)

        try {
            val response = ApiProvider.provideApiService().deleteUser(user_id)

            if (response.isSuccessful) {
                emit(ResultState.Success<DeleteUserResponse>(response.body()!!))
            } else {
                emit(ResultState.Error(Exception(response.errorBody()?.string())))
            }
        }catch (E: Exception){
            emit(ResultState.Error(E))
        }
    }

    suspend fun GetAllSells(

    ): Flow<ResultState<GetAllSellResponse>> = flow{
        emit(ResultState.Loading)

        try {
            val response = ApiProvider.provideApiService().getsellhistory()
            if (response.isSuccessful){
                emit(ResultState.Success<GetAllSellResponse>(response.body()!!))
            }else{
                emit(ResultState.Error(Exception(response.errorBody()?.string())))
            }
        }catch (e: Exception){
            emit(ResultState.Error(e))



        }
    }

    suspend fun cretaeSellOrder(
        user_id: String,
        product_id: String,
        product_name: String,
        user_name: String,
        remaining_stock: String,
        total_amount: String,
        price: String,
        quantity: String

    ): Flow<ResultState<CreateSellOrderResponse>> = flow{


        emit(ResultState.Loading)

        try {
            val response=ApiProvider.provideApiService().createSellOrder(
                user_id,
                product_id,
                product_name,
                user_name,
                remaining_stock,
                total_amount,
                price,
                quantity

            )

            if (response.isSuccessful){
                emit(ResultState.Success<CreateSellOrderResponse>(response.body()!!))
            }else {
                emit(ResultState.Error(Exception(response.errorBody()?.string())))

            }
        }
        catch (E:Exception){

            emit(ResultState.Error(E))
        }
    }




    suspend fun updateuserdeatils(
        user_id: String,
        user_name: String,
        email: String,
        password: String,
        phonenumber: String,
        address: String,
        pincode: String,

    ): Flow<ResultState<UpdateUserDetailsResponse>> = flow{
        emit(ResultState.Loading)

        val response=ApiProvider.provideApiService().edituserdetails(
            user_id,
            user_name,
            email,
            password,
            phonenumber,
            address,
            pincode
        )


        try {
            if (response.isSuccessful){
                emit(ResultState.Success<UpdateUserDetailsResponse>(response.body()!!))
            }else {
                emit(ResultState.Error(Exception(response.errorBody()?.string())))

            }
        }
            catch (e: Exception){
                emit(ResultState.Error(e))
            }


    }
}