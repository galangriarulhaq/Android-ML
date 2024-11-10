package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.CancerEntity

@Dao
interface CancerDao {
    @Query("SELECT * FROM cancers")
    fun getCancers(): LiveData<List<CancerEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCancer(event: List<CancerEntity>)

    @Delete()
    suspend fun delete(cancer: CancerEntity)
}