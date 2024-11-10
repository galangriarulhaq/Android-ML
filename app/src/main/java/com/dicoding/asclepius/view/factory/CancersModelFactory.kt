package com.dicoding.asclepius.view.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.local.CancerRepository
import com.dicoding.asclepius.di.Injection
import com.dicoding.asclepius.view.model.CancersModel

class CancersModelFactory private constructor(private val cancerRepository: CancerRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CancersModel::class.java)) {
            return CancersModel(cancerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: CancersModelFactory? = null
        fun getInstance(context: Context): CancersModelFactory =
            instance ?: synchronized(this) {
                instance ?: CancersModelFactory(Injection.provideCancerRepository(context))
            }.also { instance = it }
    }
}