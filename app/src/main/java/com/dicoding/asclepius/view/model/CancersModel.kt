package com.dicoding.asclepius.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.CancerRepository
import com.dicoding.asclepius.data.local.entity.CancerEntity
import kotlinx.coroutines.launch

class CancersModel(private val repository: CancerRepository) : ViewModel() {

    val cancers: LiveData<List<CancerEntity>> = repository.getCancers()


    fun insertCancers(cancers: List<CancerEntity>) = viewModelScope.launch {
        repository.insertCancers(cancers)
    }

    fun deleteCancer(cancer: CancerEntity) = viewModelScope.launch {
        repository.deleteCancer(cancer)
    }
}