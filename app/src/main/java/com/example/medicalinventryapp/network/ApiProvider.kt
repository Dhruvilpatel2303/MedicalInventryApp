package com.example.medicalinventryapp.network

import com.example.medicalinventryapp.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiProvider {


    fun provideApiService()= Retrofit.Builder().client(OkHttpClient.Builder().build()).baseUrl(BASE_URL).addConverterFactory(
        GsonConverterFactory.create()).build().create(ApiService::class.java)
}