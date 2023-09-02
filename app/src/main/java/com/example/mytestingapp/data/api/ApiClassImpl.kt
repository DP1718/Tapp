package com.example.mytestingapp.data.api

import com.example.mytestingapp.data.model.PhotoDataModel
import com.example.mytestingapp.db.AppDatabase
import com.example.mytestingapp.db.PhotoDataDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class ApiClassImpl @Inject constructor(private val apiService: ApiService): ApiHelper {

    override suspend fun getDataFromApi(url: String): Response<List<PhotoDataModel>> = apiService.getDataFromApi(url)

    /*override suspend fun insertAll(datas: List<PhotoDataModel>): Flow<Unit> = flow {
        appDatabase.photoDao().insertData(datas)
        emit(Unit)
    }*/

   /* override suspend fun insertAll(datas: List<PhotoDataModel>): Flow<Unit> = flow {
        appDatabase.photoDao().insertData(datas)
        emit(Unit)
    }*/
}