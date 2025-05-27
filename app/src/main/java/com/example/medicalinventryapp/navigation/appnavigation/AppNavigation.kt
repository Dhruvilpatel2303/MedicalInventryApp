package com.example.medicalinventryapp.navigation.appnavigation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.medicalinventryapp.navigation.authroutes
import com.example.medicalinventryapp.ui.screens.authentication.LoginScreenUI
import com.example.medicalinventryapp.ui.screens.authentication.SignUpScreenUI
import com.example.medicalinventryapp.ui.screens.dashboard.OrderScreens.*
import com.example.medicalinventryapp.ui.screens.dashboard.ProductsScreen.AllProductScreenUI
import com.example.medicalinventryapp.ui.screens.dashboard.ProductsScreen.ProductDetailsScreenUI
import com.example.medicalinventryapp.ui.screens.dashboard.ProfileScreen.EditProfileScreenUI
import com.example.medicalinventryapp.ui.screens.dashboard.ProfileScreen.ProfileContent
import com.example.medicalinventryapp.ui.screens.dashboard.SellHistoryScreens.AllSellScreenUI
import com.example.medicalinventryapp.ui.screens.dashboard.SellHistoryScreens.CreateSellOrderScreenUI
import com.example.medicalinventryapp.ui.screens.waiting.WaitingScreenUI
import com.example.medicalinventryapp.viewModels.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val navController = rememberNavController()
    val userIdState by viewModel.getuseridstate.collectAsState()
    val specificuser = viewModel.getspecificuser.collectAsState().value.success?.isApproved

    LaunchedEffect(Unit) {
        delay(1500)
        viewModel.getUserId()
        Log.d("AppNavigation", "UserId: ${userIdState.userId}, isApproved: $specificuser")
    }

    val startScreen = remember(userIdState.userId, specificuser) {
        when {
            userIdState.userId.isNullOrEmpty() || userIdState.userId == "null" || userIdState.userId == "" -> {
                Log.d("AppNavigation", "startScreen: Login (userId=${userIdState.userId})")
                authroutes.Login
            }
            else -> {

                Log.d("AppNavigation", "startScreen: WaitingScreen (userId=${userIdState.userId})")


                authroutes.WaitingScreen(userIdState.userId!!)

            }
        }
    }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val baseRoute = currentRoute?.substringBefore("?")?.substringBefore("/")
    Log.d("AppNavigation", "Current Route: $currentRoute, Base Route: $baseRoute")

    LaunchedEffect(currentRoute) {
        selectedIndex = when (baseRoute) {
            authroutes.ProductsScreen::class.qualifiedName -> 0
            authroutes.OrderScreen::class.qualifiedName -> 1
            authroutes.SalesHistoryScreen::class.qualifiedName -> 2
            authroutes.ProfileScreen::class.qualifiedName -> 3
            else -> 0 // Default to Products
        }
    }

    val bottomBarRoutes = listOf(
        authroutes.ProductsScreen::class.qualifiedName,
        authroutes.OrderScreen::class.qualifiedName,
        authroutes.SalesHistoryScreen::class.qualifiedName,
        authroutes.ProfileScreen::class.qualifiedName
    )

    if (userIdState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "App Logo",
                        modifier = Modifier.size(84.dp)
                    )
                }
                Text(
                    text = "Medical Inventory",
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    } else {
        Scaffold(
            bottomBar = {
                if (baseRoute != null && baseRoute in bottomBarRoutes) {
                    BottomAppBar(
                        containerColor = Color(127, 71, 31, 255),
                        modifier = Modifier
                            .padding(
                                start = 10.dp,
                                end = 10.dp,
                                bottom = 10.dp
                            )
                            .height(70.dp)
                            .clip(CircleShape)
                    ) {
                        bottombar.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedIndex == index,
                                onClick = {
                                    if (selectedIndex != index) {
                                        selectedIndex = index
                                        when (index) {
                                            0 -> navController.navigate(authroutes.ProductsScreen) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
                                            1 -> navController.navigate(authroutes.OrderScreen) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                            }
                                            2 -> navController.navigate(authroutes.SalesHistoryScreen) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                            }
                                            3 -> navController.navigate(authroutes.ProfileScreen) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.name,
                                        modifier = Modifier.size(if (selectedIndex == index) 40.dp else 30.dp),
                                        tint = if (selectedIndex == index) Color.Red else Color.White
                                    )
                                },
                                label = { Text(text = item.name, color = Color.Green) },
                                alwaysShowLabel = false,
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.Green,
                                    unselectedIconColor = Color.White
                                ),
                                modifier = Modifier.padding(bottom = 0.dp, top = 20.dp)
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                BackHandler(enabled = baseRoute != null && baseRoute in bottomBarRoutes && baseRoute != authroutes.ProductsScreen::class.qualifiedName) {
                    navController.navigate(authroutes.ProductsScreen) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                    selectedIndex = 0
                }

                NavHost(
                    navController = navController,
                    startDestination = startScreen,
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeIn(
                            animationSpec = tween(
                                durationMillis = 200,
                                easing = LinearEasing
                            )
                        ) + scaleIn(
                            initialScale = 0.92f,
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            )
                        )
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { -it / 2 },
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeOut(
                            animationSpec = tween(
                                durationMillis = 200,
                                easing = LinearEasing
                            )
                        ) + scaleOut(
                            targetScale = 0.92f,
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            )
                        )
                    },
                    popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeIn(
                            animationSpec = tween(
                                durationMillis = 200,
                                easing = LinearEasing
                            )
                        ) + scaleIn(
                            initialScale = 0.92f,
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            )
                        )
                    },
                    popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeOut(
                            animationSpec = tween(
                                durationMillis = 200,
                                easing = LinearEasing
                            )
                        ) + scaleOut(
                            targetScale = 0.92f,
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                ) {
                    composable<authroutes.SignUp> { SignUpScreenUI(viewModel, navController) }
                    composable<authroutes.Login> { LoginScreenUI(viewModel, navController) }
                    composable<authroutes.WaitingScreen> {
                        val data = it.toRoute<authroutes.WaitingScreen>()
                        WaitingScreenUI(data.userID, viewModel, navController)
                    }
                    composable<authroutes.ProductsScreen> {
                        AllProductScreenUI(viewModel, navController)
                    }
                    composable<authroutes.ProductDetailsScreen> {
                        val data = it.toRoute<authroutes.ProductDetailsScreen>()
                        ProductDetailsScreenUI(data.product_Id, viewModel, navController)
                    }
                    composable<authroutes.ProfileScreen> {
                        ProfileContent(navController, viewModel)
                    }
                    composable<authroutes.OrderScreen> {
                        OrderScreenUI(viewModel, navController)
                    }
                    composable<authroutes.SalesHistoryScreen> {
                        AllSellScreenUI(viewModel, navController)
                    }
                    composable<authroutes.AllOrdersScreen> {
                        OrderScreenUI(viewModel, navController)
                    }
                    composable<authroutes.CreateOrderScreen> {
                        CreateOrderScreenUI(viewModel, navController)
                    }
                    composable<authroutes.MakeOrderScreen> {
                        val data = it.toRoute<authroutes.MakeOrderScreen>()
                        MakeOrderScreenUI(data.product_Id, viewModel, navController)
                    }
                    composable<authroutes.EditOrderScreen> {
                        val data = it.toRoute<authroutes.EditOrderScreen>()
                        EditOrderScreenUI(data.order_Id, viewModel, navController)
                    }
                    composable<authroutes.SellHistoryScreen> {
                        CreateSellOrderScreenUI(viewModel, navController)
                    }
                    composable<authroutes.EditProfileScreen> {
                        EditProfileScreenUI(viewModel, navController)
                    }
                }
            }
        }
    }
}

data class BottomBarItem(
    val name: String,
    val icon: ImageVector,
)

val bottombar = listOf(
    BottomBarItem("Products", Icons.Filled.Home),
    BottomBarItem("Order", Icons.Filled.ShoppingCart),
    BottomBarItem("History", Icons.Filled.Menu),
    BottomBarItem("Profile", Icons.Filled.Person)
)