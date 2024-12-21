package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val storyId=intent.getStringExtra("ID_STORY")
        Log.d("ID STORY","$storyId")
        viewModel.getSession().observe(this, Observer { user->
            val token = "Bearer "+user.token
            storyId?.let { viewModel.getDetailStories(token, it) }
            viewModel.detailStories.observe(this, Observer { detail->
                binding.tvPlacename.text=detail.story?.name
                binding.tvDescription.text=detail.story?.description
                Glide.with(this)
                    .load(detail.story?.photoUrl)
                    .into(binding.tvImage)
            })
        })
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}