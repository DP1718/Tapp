package com.example.mytestingapp.data.api

import com.example.mytestingapp.data.model.PhotoDataModel
import retrofit2.Response

interface ApiHelper {

    suspend fun getDataFromApi(url:String): Response<List<PhotoDataModel>>
}