package com.dicoding.asclepius.view.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.remote.NewsRepository
import com.dicoding.asclepius.data.remote.Result
import com.dicoding.asclepius.data.remote.response.ArticlesItem

class NewsModel(private val newsRepository: NewsRepository) : ViewModel() {
    private val _listNews = MutableLiveData<List<ArticlesItem>>()
    val listNews: MutableLiveData<List<ArticlesItem>> = _listNews

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchNews()
    }

    private fun fetchNews() {
        _isLoading.value = true
        newsRepository.fetchNews().observeForever { result ->
            when (result) {
                is Result.Loading -> _isLoading.value = true
                is Result.Success -> {
                    _isLoading.value = false
                    _listNews.value = result.data
                }

                is Result.Error -> {
                    _isLoading.value = false
                    Log.e(TAG, "Error: ${result.error}")
                }
            }
        }

    }

    companion object {
        private const val TAG = "NewsModel"
    }
}