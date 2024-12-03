package com.dicoding.picodiploma.loginwithanimation.view.addstory

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityAddStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.btnCamera.setOnClickListener {
            Toast.makeText(this@AddStoryActivity, "Under Development", Toast.LENGTH_SHORT).show()
        }
        binding.btnPost.setOnClickListener {
            val imageFile = imageUri?.let { it1 -> uriToFile(it1, this) }
            val description = binding.etDescription.text.toString()
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile?.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = requestImageFile?.let { reqImage ->
                MultipartBody.Part.createFormData(
                    "photo",
                    imageFile?.name,
                    reqImage
                )
            }
            viewModel.getSession().observe(this, Observer {user->
                val token = "Bearer "+user.token
                multipartBody?.let { it1 -> viewModel.addNewStory(requestBody, it1,token) }
            })
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery =registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){ uri: Uri? ->
        uri?.let {
            binding.imagePlace.setImageURI(uri)
            imageUri = uri
        }
    }

    fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    fun createCustomTempFile(context: Context): File {
        return try {
            // Buat file sementara di direktori cache aplikasi
            File.createTempFile("temp_image_", ".jpg", context.cacheDir)
        } catch (e: IOException) {
            e.printStackTrace()
            // Jika terjadi kesalahan, kembalikan file kosong
            File(context.cacheDir, "temp_image.jpg")
        }
    }
}