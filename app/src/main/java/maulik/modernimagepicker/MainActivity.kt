package maulik.modernimagepicker

import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import maulik.modernimagepicker.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var cameraImageFile: File? = null
    private val viewModel by viewModels<MainViewModel>()

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            viewModel.onGalleryImagePicked(it)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            cameraImageFile?.let { file ->
                viewModel.onCameraImagePicked(file)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.cardCamera.setOnClickListener { openCamera() }
        binding.cardGallery.setOnClickListener { openGallery() }
        binding.ivClear.setOnClickListener { binding.motionContainer.transitionToState(R.id.start) }

        viewModel.imageBitmapLiveData.observe(this) {
            binding.ivGallery.setImageBitmap(it)
            binding.motionContainer.transitionToState(R.id.end)
        }
        viewModel.imageFileLiveData.observe(this) {
            Log.d("ImageFile", it?.absolutePath.toString())
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun openCamera() {
        cameraImageFile = getTempFile()
        cameraImageFile?.let {
            val imageUri = FileProvider.getUriForFile(
                this,
                "${BuildConfig.APPLICATION_ID}.fileprovider",
                it
            )
            cameraLauncher.launch(imageUri)
        }

    }
}