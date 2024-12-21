package com.dicoding.picodiploma.loginwithanimation.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
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

class MainViewModel(private val repositoryUser: UserRepository, private val repositoryStory: StoryRepository ) : ViewModel() {
    private val _allStories = MutableLiveData<List<ListStoryItem>?>()
    val allStories: LiveData<List<ListStoryItem>?> = _allStories
    private val _detailStories = MutableLiveData<DetailStoryResponse>()
    val detailStories: LiveData<DetailStoryResponse> = _detailStories
    private val _addStoryResponse = MutableLiveData<AddStoryResponse>()
    val addStoryResponse: LiveData<AddStoryResponse> = _addStoryResponse

    fun getSession(): LiveData<UserModel> {
        return repositoryUser.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repositoryUser.logout()
        }
    }

    fun getAllStories(authToken: String): LiveData<PagingData<ListStoryItem>> = repositoryStory.getAllStories(authToken).cachedIn(viewModelScope)

    fun getAllStoriesWithMarker(authToken: String){
        val client = ApiConfig.getApiService().getAllStoriesWithMarker(authToken)
        client.enqueue(object : retrofit2.Callback<GetStoryResponse>{
            override fun onResponse(call: Call<GetStoryResponse>, response: Response<GetStoryResponse>) {
                if (response.isSuccessful) {
                    _allStories.value = response.body()?.listStory
                    Log.e("Get Stories", "Succesfully Fetch All Stories")
                }else{
                    Log.e("Get Stories", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetStoryResponse>, t: Throwable) {
                Log.e("Get Stories", "onFailure: ${t.message.toString()}")
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

    fun addNewStory(
        description: RequestBody, photo: MultipartBody.Part,
        authToken: String){
        val client = ApiConfig.getApiService().addNewStory(description, photo, authToken)
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

    fun addNewStoryWithLoc(
        description: RequestBody, lat: Float, lon: Float, photo: MultipartBody.Part,
        authToken: String){
        val client = ApiConfig.getApiService().addNewStoryWithLoc(description, lat, lon, photo, authToken)
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