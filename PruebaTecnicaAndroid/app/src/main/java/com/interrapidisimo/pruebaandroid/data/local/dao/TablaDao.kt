package com.interrapidisimo.pruebaandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.interrapidisimo.pruebaandroid.data.local.entity.TablaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TablaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTablas(tablas: List<TablaEntity>)

    @Query("SELECT * FROM tablas")
    fun getTablas(): Flow<List<TablaEntity>>

    @Query("DELETE FROM tablas")
    suspend fun clearTablas()
}
