package com.example.mytestingapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mytestingapp.data.model.PhotoDataModel

@Dao
interface PhotoDataDao {

    @Insert
    suspend fun insertData(photoDataModel: List<PhotoDataModel>)

//    @Query("SELECT * FROM items")
//    suspend fun getAllPhotoData(): List<PhotoDataModel>

    @Query("SELECT * FROM PhotoDataModel")
    suspend fun getDataCall():List<PhotoDataModel>

    @Query("UPDATE PhotoDataModel SET downloadUrl = :filePath where id= :mID")
    suspend fun updateRecord(mID: String, filePath: String)
}