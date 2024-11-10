package com.dicoding.asclepius.view

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.CancerEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.view.model.CancersModel
import com.dicoding.asclepius.view.factory.CancersModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    private val cancersModel: CancersModel by viewModels {
        CancersModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includeToolbar.toolbar)
        supportActionBar?.title = getString(R.string.result)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)

        binding.includeToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val currentTime = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedTime = formatter.format(currentTime)


        val resultText = intent.getStringExtra("RESULT_TEXT")
        val imageUriString = intent.getStringExtra("IMAGE_URI")
        val inferenceTime = intent.getLongExtra("INFERENCE_TIME", 0L)

        binding.tvResult.text = getString(R.string.result_text, resultText, inferenceTime, formattedTime)

        binding.btnSave.setOnClickListener {
            val cancerEntity = CancerEntity(
                mediaCover = imageUriString ?: "",
                title = resultText ?: "",
                date = formattedTime,
                inference = inferenceTime.toString()
            )
            cancersModel.insertCancers(listOf(cancerEntity))
            Toast.makeText(this, "Data Saved successfully", Toast.LENGTH_SHORT).show()
            onBackPressedDispatcher.onBackPressed()
        }

        imageUriString?.let {
            val imageUri = Uri.parse(it)
            binding.resultImage.setImageURI(imageUri)
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
}
