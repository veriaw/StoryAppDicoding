package com.dicoding.picodiploma.loginwithanimation.view.main

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.data.StoryPagingSource
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.AddStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.GetStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import kotlinx.coroutines.flow.Flow
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
    private val _addStoryResponse = MutableLiveData<AddStoryResponse>()
    val addStoryResponse: LiveData<AddStoryResponse> = _addStoryResponse

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getAllStories(authToken: String): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, // Ukuran halaman
                enablePlaceholders = false // Jangan gunakan placeholder
            ),
            pagingSourceFactory = { StoryPagingSource(authToken) }
        ).flow.cachedIn(viewModelScope)
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
                    _addStoryResponse.value=response.body()
                }else{
                    Log.e("Add Stories", "Test: ${response.message()}")
                    _addStoryResponse.value = AddStoryResponse(error = true, message = response.message())
                }
                Log.d("MAIN VIEW ADD STORY","${response.body()}")
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                Log.e("Add Stories", "onFailure: ${t.message.toString()}")
            }

        })
    }
}