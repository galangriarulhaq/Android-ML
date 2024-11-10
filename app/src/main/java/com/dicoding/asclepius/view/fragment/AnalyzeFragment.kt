package com.dicoding.asclepius.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dicoding.asclepius.R
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.ResultActivity
import com.dicoding.asclepius.view.model.AnalyzeModel
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.gms.vision.classifier.Classifications
import java.io.File

class AnalyzeFragment : Fragment() {

    private lateinit var imageView: ImageView

    private val analyzeModel: AnalyzeModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_analyze, container, false)

        imageView = view.findViewById(R.id.previewImageView)


        analyzeModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                showImage(it)
            }
        }

        val galleryButton = view.findViewById<View>(R.id.galleryButton)
        galleryButton.setOnClickListener {
            startGallery()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val analyzeButton = view.findViewById<View>(R.id.analyzeButton)
        analyzeButton.setOnClickListener {
            analyzeImage()
        }

    }

    private fun analyzeImage() {
        analyzeModel.getImageUri()?.let { uri ->
            val classifierHelper = ImageClassifierHelper(
                context = requireContext(),
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        showToast("Error: $error")
                    }

                    override fun onResults(results: List<Classifications>, inferenceTime: Long) {
                        val resultText = results.joinToString("\n") { classification ->
                            classification.categories.joinToString(", ") {
                                "${it.label} ${"%.2f".format(it.score * 100)}%"
                            }
                        }
                        moveToResult(uri, resultText, inferenceTime)
                    }

                }
            )
            classifierHelper.classifyStaticImage(uri)
        } ?: run {
            showToast("There are no images to analyze")
        }
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            startCrop(uri)
        } else {
            showToast("No image selected")
        }
    }

    private fun startCrop(uri: Uri) {
        val fileName = "croppedImage_${System.currentTimeMillis()}.png"
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, fileName))
        val uCrop = UCrop.of(uri, destinationUri)
        uCrop.withAspectRatio(1f, 1f)
        uCrop.withMaxResultSize(1080, 1080)

        val cropIntent = uCrop.getIntent(requireContext())
        cropResultLauncher.launch(cropIntent)
    }

    private val cropResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            resultUri?.let {
                analyzeModel.setImageUri(it)
                showImage(it)
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(result.data!!)
            cropError?.let {
                showToast("Crop error: ${it.message}")
            }
        }
    }

    private fun showImage(resultUri: Uri) {
        imageView.setImageDrawable(null)
        imageView.setImageURI(resultUri)
        imageView.invalidate()
    }

    private fun moveToResult(imageUri: Uri, resultText: String, inferenceTime: Long) {
        val intent = Intent(requireContext(), ResultActivity::class.java).apply {
            putExtra("RESULT_TEXT", resultText)
            putExtra("IMAGE_URI", imageUri.toString())
            putExtra("INFERENCE_TIME", inferenceTime)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}