package com.dicoding.asclepius.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.util.DialogUtil.showNoInternetDialog
import com.dicoding.asclepius.util.Network
import com.dicoding.asclepius.view.adapter.NewsAdapter
import com.dicoding.asclepius.view.model.NewsModel
import com.dicoding.asclepius.view.factory.NewsModelFactory


class NewsFragment : Fragment() {

    private val newsModel: NewsModel by viewModels {
        NewsModelFactory.getInstance(requireActivity())
    }

    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Network.check(requireContext())) {
            setupRecyclerView(view)
            observeViewModels()
        } else {
            showNoInternetDialog(requireContext(), layoutInflater)
        }

    }

    private fun observeViewModels() {

        newsModel.listNews.observe(viewLifecycleOwner) { news ->
            newsAdapter.submitList(news)
        }

        newsModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            updateLoadingState(isLoading)
        }

    }

    private fun setupRecyclerView(view: View) {

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_news)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(onItemClick = { url ->
            if (url.isNotEmpty()) {
                navigateToDetailArticle(url)
            }
        })

        recyclerView.adapter = newsAdapter

    }


    private fun updateLoadingState(isLoading: Boolean) {

        val progressBar = view?.findViewById<ProgressBar>(R.id.progressIndicator)
        val rvEvent = view?.findViewById<RecyclerView>(R.id.rv_news)

        if (isLoading) {
            progressBar?.visibility = View.VISIBLE
            rvEvent?.visibility = View.GONE
        } else {
            progressBar?.visibility = View.GONE
            rvEvent?.visibility = View.VISIBLE
        }

    }

    private fun navigateToDetailArticle(url: String) {

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)

    }

}