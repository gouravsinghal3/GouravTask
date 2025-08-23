package com.example.gouravtask.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gouravtask.data.db.entity.Holding
import kotlinx.coroutines.flow.Flow

@Dao
interface HoldingsDao {

    @Query("SELECT * FROM holdings")
    fun getAllHoldings(): Flow<List<Holding>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(holdings: List<Holding>)

    @Query("DELETE FROM holdings")
    suspend fun clearAll()
}