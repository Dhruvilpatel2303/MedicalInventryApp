package com.example.medicalinventryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.medicalinventryapp.navigation.appnavigation.AppNavigation

import com.example.medicalinventryapp.pref.PrefrenceDatastore
import com.example.medicalinventryapp.ui.screens.dashboard.SellHistoryScreens.AllSellScreenUI

import com.example.medicalinventryapp.ui.theme.MedicalInventryAppTheme
import com.example.medicalinventryapp.viewModels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context= LocalContext.current
            val prefrenceDatastore= PrefrenceDatastore(context)
            val viewModel= MainViewModel(prefrenceDatastore)

            MedicalInventryAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


//
//                    AppNavigation(viewModel)

//                    AllSellScreenUI(viewModel, navController)


                    AppNavigation(viewModel)

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MedicalInventryAppTheme {
        Greeting("Android")
    }
}