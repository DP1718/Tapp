package com.example.mytestingapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mytestingapp.data.model.PhotoDataModel

@Database(entities = [PhotoDataModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDataDao
}