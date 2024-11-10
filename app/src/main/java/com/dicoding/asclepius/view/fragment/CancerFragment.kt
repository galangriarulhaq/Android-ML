package com.dicoding.asclepius.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.view.adapter.CancersAdapter
import com.dicoding.asclepius.view.model.CancersModel
import com.dicoding.asclepius.view.factory.CancersModelFactory


class CancerFragment : Fragment() {

    private val cancersModel: CancersModel by viewModels {
        CancersModelFactory.getInstance(requireActivity())
    }

    private lateinit var cancersAdapter: CancersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cancer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        observeViewModels()
    }

    private fun observeViewModels() {

        cancersModel.cancers.observe(viewLifecycleOwner) { cancers ->
            cancersAdapter.submitList(cancers)

            val imgEmpty = view?.findViewById<ImageView>(R.id.img_empty)
            val recyclerView = view?.findViewById<RecyclerView>(R.id.rv_cancers)
            if (cancers.isNullOrEmpty()) {
                imgEmpty?.visibility = View.VISIBLE
                recyclerView?.visibility = View.GONE
            } else {
                imgEmpty?.visibility = View.GONE
                recyclerView?.visibility = View.VISIBLE
            }
        }

    }

    private fun setupRecyclerView(view: View) {

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_cancers)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        cancersAdapter = CancersAdapter(onDeleteClick = { item ->
            cancersModel.deleteCancer(item)
            Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
        })

        recyclerView.adapter = cancersAdapter

    }


}