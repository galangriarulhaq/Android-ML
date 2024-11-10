package com.dicoding.asclepius.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.entity.CancerEntity

@Database(entities = [CancerEntity::class], version = 1, exportSchema = false)
abstract class CancerDatabase : RoomDatabase() {
    abstract fun cancerDao(): CancerDao

    companion object {
        @Volatile
        private var instance: CancerDatabase? = null
        fun getInstance(context: Context): CancerDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    CancerDatabase::class.java, "Cancers.db"
                ).build()
            }
    }
}