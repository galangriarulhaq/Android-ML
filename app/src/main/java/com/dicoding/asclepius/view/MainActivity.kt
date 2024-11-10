package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.view.fragment.CancerFragment
import com.dicoding.asclepius.view.fragment.AnalyzeFragment
import com.dicoding.asclepius.view.fragment.NewsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val SELECTED_FRAGMENT_KEY = "selected_fragment"

    private var selectedFragmentIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.includeToolbar.toolbar)

        if (savedInstanceState != null) {
            selectedFragmentIndex = savedInstanceState.getInt(SELECTED_FRAGMENT_KEY, 1)
        } else {
            selectedFragmentIndex = 1
        }


        loadFragment(selectedFragmentIndex)


        binding.bottomBar.itemActiveIndex = selectedFragmentIndex


        binding.bottomBar.setOnItemSelectedListener { position ->
            if (position != selectedFragmentIndex) {
                selectedFragmentIndex = position
                loadFragment(selectedFragmentIndex)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom,
            )
            insets
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_FRAGMENT_KEY, selectedFragmentIndex)
    }

    private fun loadFragment(index: Int) {
        val selectedFragment = when (index) {
            0 -> NewsFragment()
            1 -> AnalyzeFragment()
            2 -> CancerFragment()
            else -> AnalyzeFragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment, selectedFragment)
            .commit()
    }


}