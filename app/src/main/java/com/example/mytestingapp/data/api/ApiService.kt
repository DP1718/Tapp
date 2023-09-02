package com.example.mytestingapp.data.api

import com.example.mytestingapp.data.model.PhotoDataModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET
    suspend fun getDataFromApi(@Url url:String): Response<List<PhotoDataModel>>


}
