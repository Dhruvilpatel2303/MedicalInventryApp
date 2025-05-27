package com.example.medicalinventryapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalinventryapp.Repositry.Repo
import com.example.medicalinventryapp.common.ResultState
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
import com.example.medicalinventryapp.pref.PrefrenceDatastore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val prefrenceDatastore: PrefrenceDatastore
) : ViewModel() {

    private val repo = Repo()

    private val _getuseridstate = MutableStateFlow<GetUserId>(GetUserId())
    val getuseridstate = _getuseridstate.asStateFlow()

    private val _createuserstate = MutableStateFlow<CreateUserState>(CreateUserState())
    val createuserstate = _createuserstate.asStateFlow()


    private val _loginuserstate = MutableStateFlow<LoginUserState>(LoginUserState())
    val loginuserstate = _loginuserstate.asStateFlow()

    private val _getspecificuser = MutableStateFlow<GetSpecificUser>(GetSpecificUser())
    val getspecificuser = _getspecificuser.asStateFlow()

    private val _getallproductsstate = MutableStateFlow<GetAllProducts>(GetAllProducts())
    val getallproductstate = _getallproductsstate.asStateFlow()

    private val _getspecificproductstate =
        MutableStateFlow<GetSpecificProduct>(GetSpecificProduct())
    val getspecificproductstate = _getspecificproductstate.asStateFlow()


    private val _updateuserdetailsstate = MutableStateFlow<UpdateUserDetails>(UpdateUserDetails())
    val updateuserdetailsstate = _updateuserdetailsstate.asStateFlow()


    //oders functions and states

    private val _creteorderstate = MutableStateFlow<CreateOrder>(CreateOrder())
    val createorderstate = _creteorderstate.asStateFlow()

    private val _getallordersstate = MutableStateFlow<GetAllOrders>(GetAllOrders())
    val getallordersstate = _getallordersstate.asStateFlow()

    private val _updateorderstate = MutableStateFlow<UpdateOrder>(UpdateOrder())
    val updateorderstate = _updateorderstate.asStateFlow()

    private val _getspecificorderstate = MutableStateFlow<GetSpecificOrder>(GetSpecificOrder())
    val getspecificorderstate = _getspecificorderstate.asStateFlow()


    private val _cancelorderstate = MutableStateFlow<CancelOrder>(CancelOrder())
    val cancelorderstate = _cancelorderstate.asStateFlow()


    private val _deleteuserstate = MutableStateFlow<DeleteAccount>(DeleteAccount())
    val deleteuserstate = _deleteuserstate.asStateFlow()


    //all sells history details and their creation states
    private val _getallsellsstate = MutableStateFlow<GetAllSell>(GetAllSell())
    val getallsellstate = _getallsellsstate.asStateFlow()

    private val _creatsellorderstate = MutableStateFlow<CreateSellOrder>(CreateSellOrder())
    val creatsellorderstate = _creatsellorderstate.asStateFlow()


    init {

        getUserId()


    }


    fun getUserId() {
        viewModelScope.launch(Dispatchers.IO) {
            prefrenceDatastore.getUserId.collect { userId ->
                Log.d("MainViewModel", "Collected userId: $userId")

                _getuseridstate.value = GetUserId(userId = userId.toString(), isLoading = false)
                Log.d("MainViewModel", "Updated userIdState: ${_getuseridstate.value}")
            }
        }
    }


    fun deleteAcount(user_id: String) {

        viewModelScope.launch(Dispatchers.IO) {
            repo.DeleteUser(user_id).collect {
                when (it) {
                    is ResultState.Success -> {
                        prefrenceDatastore.clearUserId()
                        Log.d("TAG", "deleteAcount: ${it.data}")
                        _deleteuserstate.value = DeleteAccount(success = it.data, isLoading = false)
                    }

                    is ResultState.Error -> {
                        Log.d("TAG", "deleteAcount: ${it.exception.message}")
                        _deleteuserstate.value =
                            DeleteAccount(error = it.exception.message, isLoading = false)
                    }

                    is ResultState.Loading -> {
                        _deleteuserstate.value = DeleteAccount(isLoading = true)
                    }
                }
            }
        }
    }


    fun createUser(
        email: String,
        password: String,
        name: String,
        phonenumber: String,
        address: String,
        pincode: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.createUser(
                name = name,
                email = email,
                password = password,
                phonenumber = phonenumber,
                address = address,
                pincode = pincode
            ).collect {
                when (it) {

                    is ResultState.Success -> {
                        if (it.data.status.toString() == "200") {
                            prefrenceDatastore.saveUserId(userId = it.data.message)
                        }
                        _createuserstate.value =
                            CreateUserState(success = it.data, isLoading = false)


                    }

                    is ResultState.Error -> {
                        _createuserstate.value =
                            CreateUserState(error = it.exception.message, isLoading = false)

                    }

                    is ResultState.Loading -> {
                        _createuserstate.value = CreateUserState(isLoading = true)

                    }
                }
            }
        }

    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            prefrenceDatastore.saveUserId("") // Clear the userId
            _getuseridstate.value = GetUserId(userId = null, isLoading = false) // Reset the state
        }
    }

    fun LoginUser(
        email: String,
        password: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {


            repo.LoginUser(email, password).collect {

                when (it) {
                    is ResultState.Success -> {
                        if (it.data.status == 200) {
                            viewModelScope.launch(Dispatchers.IO) {
                                prefrenceDatastore.saveUserId(userId = it.data.message)
                                getSpecificUserDetails(userId = it.data.message)
//                                prefrenceDatastore.saveIsUserApproved(getspecificuser.value.success!!.isApproved.toString())
                            }
                        }


                        _loginuserstate.value =
                            LoginUserState(success = it.data, isLoading = false)

                    }

                    is ResultState.Error -> {

                        _loginuserstate.value =
                            LoginUserState(error = it.exception.message, isLoading = false)
                    }

                    is ResultState.Loading -> {

                        _loginuserstate.value = LoginUserState(isLoading = true)
                    }


                }
            }
        }

    }

    fun getSpecificUserDetails(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.WaitingUser(userId).collect {
                when (it) {
                    is ResultState.Success -> {
                        Log.d("WaitingUser", "Data: ${it.data}")
                        _getspecificuser.value =
                            GetSpecificUser(success = it.data, isLoading = false)
                    }

                    is ResultState.Error -> {
                        _getspecificuser.value =
                            GetSpecificUser(error = it.exception.message, isLoading = false)
                    }

                    is ResultState.Loading -> {
                        _getspecificuser.value = GetSpecificUser(isLoading = true)
                    }
                }
            }
        }

    }

    fun resetLoginState() {
        _loginuserstate.value = LoginUserState()
    }

    fun resetCreateUserState() {
        _createuserstate.value = CreateUserState()
    }

    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllProducts().collect {
                when (it) {
                    is ResultState.Success -> {
                        Log.d("TAG", "getAllProducts: ${it.data}")
                        _getallproductsstate.value =
                            GetAllProducts(success = it.data, isLoading = false)
                    }

                    is ResultState.Error -> {
                        Log.d("TAG", "getAllProducts: ${it.exception.message}")
                        _getallproductsstate.value =
                            GetAllProducts(error = it.exception.message, isLoading = false)
                    }

                    is ResultState.Loading -> {
                        _getallproductsstate.value = GetAllProducts(isLoading = true)
                    }
                }
            }
        }
    }

    fun getSpecificProductResponse(product_Id: String) {
        viewModelScope.launch(Dispatchers.IO) {

            repo.getSpecificProduct(product_Id).collect {
                when (it) {
                    is ResultState.Success -> {
                        _getspecificproductstate.value =
                            GetSpecificProduct(success = it.data, isLoading = false)
                        Log.d("TAG4", "getproduct: ${it.data}")
                    }

                    is ResultState.Error -> {
                        _getspecificproductstate.value =
                            GetSpecificProduct(error = it.exception.message, isLoading = false)
                    }

                    is ResultState.Loading -> {
                        _getspecificproductstate.value = GetSpecificProduct(isLoading = true)
                    }
                }


            }
        }

    }


    fun createOrder(
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
    ) {

        viewModelScope.launch(Dispatchers.IO) {

            repo.CreateOrder(
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
            ).collect {
                when (it) {
                    is ResultState.Success -> {
                        _creteorderstate.value =
                            CreateOrder(success = it.data, isLoading = false)
                    }

                    is ResultState.Error -> {
                        _creteorderstate.value =
                            CreateOrder(error = it.exception.message, isLoading = false)
                    }

                    is ResultState.Loading -> {
                        _creteorderstate.value = CreateOrder(isLoading = true)
                    }

                }

            }

        }

    }

    fun getAllOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllOrders().collect {
                when (it) {
                    is ResultState.Success -> {
                        _getallordersstate.value =
                            GetAllOrders(success = it.data, isLoading = false)
                    }

                    is ResultState.Error -> {
                        _getallordersstate.value =
                            GetAllOrders(error = it.exception.message, isLoading = false)
                    }

                    is ResultState.Loading -> {
                        _getallordersstate.value = GetAllOrders(isLoading = true)
                    }
                }
            }
        }
    }

    fun updateOrderDetails(
        order_id: String,
        order_quantity: String,
        message: String,
        total_amount: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateOrderDetails(order_id, order_quantity, message, total_amount).collect {

                when (it) {


                    is ResultState.Success -> {
                        _updateorderstate.value =
                            UpdateOrder(success = it.data, isLoading = false)
                        Log.d("TAG7", "updateOrderDetails: ${it.data}")
                    }

                    is ResultState.Error -> {
                        _updateorderstate.value =
                            UpdateOrder(error = it.exception.message, isLoading = false)
                        Log.d("TAG", "updateOrderDetails: ${it.exception.message}")
                    }

                    is ResultState.Loading -> {
                        _updateorderstate.value = UpdateOrder(isLoading = true)
                        Log.d("TAG", "updateOrderDetails: Loading ")

                    }
                }


            }


        }
    }


    fun getSpecificOrderDetails(order_id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getSpecificOrderDetails(order_id).collect {

                when (it) {

                    is ResultState.Success -> {
                        _getspecificorderstate.value =
                            GetSpecificOrder(success = it.data, isLoading = false)
                    }

                    is ResultState.Error -> {
                        _getspecificorderstate.value =
                            GetSpecificOrder(error = it.exception.message, isLoading = false)
                    }

                    is ResultState.Loading -> {
                        _getspecificorderstate.value = GetSpecificOrder(isLoading = true)
                    }
                }


            }


        }
    }


    fun cancelOrder(
        order_id: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            repo.CancelOrder(order_id).collect {
                when (it) {
                    is ResultState.Success -> {
                        _cancelorderstate.value =
                            CancelOrder(success = it.data, isLoading = false)
                    }

                    is ResultState.Error -> {
                        _cancelorderstate.value =
                            CancelOrder(error = it.exception.message, isLoading = false)
                    }

                    is ResultState.Loading -> {
                        _cancelorderstate.value = CancelOrder(isLoading = true)
                    }
                }


            }


        }
    }

    fun getAllSellsHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.GetAllSells().collect {

                when (it) {
                    is ResultState.Success -> {
                        _getallsellsstate.value = GetAllSell(success = it.data, isLoading = false)

                    }

                    is ResultState.Loading -> {
                        _getallsellsstate.value = GetAllSell(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _getallsellsstate.value =
                            GetAllSell(error = it.exception.message, isLoading = false)
                    }


                }
            }


        }
    }


    fun createSellOrder(
        user_id: String,
        product_id: String,
        product_name: String,
        user_name: String,
        remaining_stock: String,
        total_amount: String,
        price: String,
        quantity: String


    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.cretaeSellOrder(
                user_id,
                product_id,
                product_name,
                user_name,
                remaining_stock,
                total_amount,
                price,
                quantity
            ).collect {


                when (it) {
                    is ResultState.Loading -> {
                        _creatsellorderstate.value = CreateSellOrder(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _creatsellorderstate.value =
                            CreateSellOrder(success = it.data, isLoading = false)
                    }

                    is ResultState.Error -> {
                        _creatsellorderstate.value =
                            CreateSellOrder(error = it.exception.message, isLoading = false)
                    }


                }
            }

        }
    }

    fun updateuserdetails(
        user_id: String,
        user_name: String,
        email: String,
        password: String,
        phonenumber: String,
        address: String,
        pincode: String,


        ) {
        viewModelScope.launch(Dispatchers.IO) {

            repo.updateuserdeatils(
                user_id,
                user_name,
                email,
                password,
                phonenumber,
                address,
                pincode
            ).collect {
                when (it) {
                    is ResultState.Success -> {
                        _updateuserdetailsstate.value =
                            UpdateUserDetails(success = it.data, isLoading = false)
                    }

                    is ResultState.Error -> {
                        _updateuserdetailsstate.value =
                            UpdateUserDetails(error = it.exception.message, isLoading = false)
                    }

                    is ResultState.Loading -> {
                        _updateuserdetailsstate.value = UpdateUserDetails(isLoading = true)
                    }
                }
            }
        }

    }


}


data class CreateUserState(
    val isLoading: Boolean = false,
    val success: CreateUserResponse? = null,
    val error: String? = null
)


data class LoginUserState(
    val isLoading: Boolean = false,
    val success: LoginUserResponse? = null,
    val error: String? = null
)

data class GetSpecificUser(
    val isLoading: Boolean = false,
    val success: WaitingUserResponse? = null,
    val error: String? = null

)

data class GetAllProducts(
    val isLoading: Boolean = false,
    val success: GetAllProductsResponse? = null,
    val error: String? = null

)

data class GetSpecificProduct(
    val isLoading: Boolean = false,
    val success: GetSpeciificProductResponse? = null,
    val error: String? = null
)

data class CreateOrder(
    val isLoading: Boolean = false,
    val success: CreateOrderResponse? = null,
    val error: String? = null
)

data class GetAllOrders(
    val isLoading: Boolean = false,
    val success: GetallOrdersResponse? = null,
    val error: String? = null
)

data class UpdateOrder(
    val isLoading: Boolean = false,
    val success: UpdateOrderResponse? = null,
    val error: String? = null
)

data class GetSpecificOrder(
    val isLoading: Boolean = false,
    val success: GetSpecificOrderResponse? = null,
    val error: String? = null
)


data class CancelOrder(
    val isLoading: Boolean = false,
    val success: CancelOrderResponse? = null,
    val error: String? = null
)

data class GetUserId(
    val isLoading: Boolean = true,
    val userId: String? = ""
)

data class DeleteAccount(
    val isLoading: Boolean = false,
    val success: DeleteUserResponse? = null,
    val error: String? = null
)

data class GetAllSell(
    val isLoading: Boolean = false,
    val success: GetAllSellResponse? = null,
    val error: String? = null

)

data class CreateSellOrder(
    val isLoading: Boolean = false,
    val success: CreateSellOrderResponse? = null,
    val error: String? = null
)

data class UpdateUserDetails(
    val isLoading: Boolean = false,
    val success: UpdateUserDetailsResponse? = null,
    val error: String? = null

)