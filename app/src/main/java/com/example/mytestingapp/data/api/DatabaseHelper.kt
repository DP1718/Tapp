package com.example.mytestingapp.data.api

import com.example.mytestingapp.data.model.PhotoDataModel
import kotlinx.coroutines.flow.Flow

interface DatabaseHelper {

    suspend fun insertAll(users: List<PhotoDataModel>): Flow<Unit>

    suspend fun updateRecordCall(mid: String, filepath:String): Flow<Unit>

    suspend fun getRecordFromDB(): Flow<List<PhotoDataModel>>
}