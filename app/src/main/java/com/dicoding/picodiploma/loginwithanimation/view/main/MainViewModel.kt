package com.dicoding.picodiploma.loginwithanimation.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.AddStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.GetStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _allStories = MutableLiveData<List<ListStoryItem>?>()
    val allStories: LiveData<List<ListStoryItem>?> = _allStories
    private val _detailStories = MutableLiveData<DetailStoryResponse>()
    val detailStories: LiveData<DetailStoryResponse> = _detailStories

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getAllStories(authToken: String){
        val client = ApiConfig.getApiService().getAllStories(authToken)
        client.enqueue(object : retrofit2.Callback<GetStoryResponse> {
            override fun onResponse(call: Call<GetStoryResponse>, response: Response<GetStoryResponse>) {
                if (response.isSuccessful) {
                    _allStories.value = response.body()?.listStory
                    Log.e("Fetch All Stories", "Succesfully Fetch All Stories")
                }else{
                    Log.e("Fetch All Stories", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetStoryResponse>, t: Throwable) {
                Log.e("Fetch All Stories", "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getDetailStories(authToken: String, id: String){
        val client = ApiConfig.getApiService().getDetailStories(authToken, id)
        client.enqueue(object : retrofit2.Callback<DetailStoryResponse>{
            override fun onResponse(call: Call<DetailStoryResponse>, response: Response<DetailStoryResponse>) {
                if (response.isSuccessful) {
                    _detailStories.value = response.body()
                    Log.e("Fetch Detail Stories", "Succesfully Fetch All Stories")
                }else{
                    Log.e("Fetch Detail Stories", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                Log.e("Fetch Detail Stories", "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun addNewStory(description: RequestBody, photo: MultipartBody.Part,authToken: String){
        val client = ApiConfig.getApiService().addNewStory(description,photo,authToken)
        client.enqueue(object : Callback<AddStoryResponse>{
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("Add Stories", "Succesfully Add Stories")
                }else{
                    Log.e("Add Stories", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                Log.e("Add Stories", "onFailure: ${t.message.toString()}")
            }

        })
    }
}