package com.example.mytestingapp.data.repository

import com.example.mytestingapp.data.api.DatabaseHelper
import com.example.mytestingapp.data.model.PhotoDataModel
import com.example.mytestingapp.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DBRepository @Inject constructor(private val appDatabase: AppDatabase) : DatabaseHelper {

    override suspend fun insertAll(mArry: List<PhotoDataModel>): Flow<Unit> = flow {
        appDatabase.photoDao().insertData(mArry)
        emit(Unit)
    }

    override suspend fun updateRecordCall(mid: String, filepath: String): Flow<Unit> = flow{
       appDatabase.photoDao().updateRecord(mid,filepath)
        emit(Unit)
    }

    override suspend fun getRecordFromDB(): Flow<List<PhotoDataModel>> = flow {
        val mList = appDatabase.photoDao().getDataCall()
        emit(mList)
    }

}