package com.example.gouravtask.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gouravtask.data.db.HoldingsDao
import com.example.gouravtask.data.db.entity.Holding

@Database(entities = [Holding::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun holdingsDao(): HoldingsDao
}