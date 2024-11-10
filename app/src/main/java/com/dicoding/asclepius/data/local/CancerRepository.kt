package com.dicoding.asclepius.data.local

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.entity.CancerEntity
import com.dicoding.asclepius.data.local.room.CancerDao

class CancerRepository private constructor(
    private val cancerDao: CancerDao,
) {

    fun getCancers(): LiveData<List<CancerEntity>> = cancerDao.getCancers()

    suspend fun insertCancers(cancers: List<CancerEntity>) {
        cancerDao.insertCancer(cancers)
    }

    suspend fun deleteCancer(cancer: CancerEntity) {
        cancerDao.delete(cancer)
    }
    
    companion object {
        @Volatile
        private var instance: CancerRepository? = null
        fun getInstance(
            cancerDao: CancerDao
        ): CancerRepository =
            instance ?: synchronized(this) {
                instance ?: CancerRepository(cancerDao)
            }.also { instance = it }
    }
}