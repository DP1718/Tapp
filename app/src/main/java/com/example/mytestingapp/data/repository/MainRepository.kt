package com.example.mytestingapp.data.repository

import com.example.mytestingapp.data.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {


    suspend fun getDataFromApi(url: String) = apiHelper.getDataFromApi(url)

}