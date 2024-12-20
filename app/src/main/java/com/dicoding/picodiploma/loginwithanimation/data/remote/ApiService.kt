package com.dicoding.picodiploma.loginwithanimation.data.remote

import com.dicoding.picodiploma.loginwithanimation.data.remote.response.AddStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.GetStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.RegisterResponse
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    fun loginUser(
        @Body jsonObject: JsonObject
    ): Call<LoginResponse>

    @POST("register")
    fun registerUser(
        @Body jsonObject: JsonObject
    ): Call<RegisterResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") authToken: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int = 1,
    ): GetStoryResponse

    @GET("stories/{id}")
    fun getDetailStories(
        @Header("Authorization") authToken: String,
        @Path("id") id:String
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("stories") // Ganti dengan endpoint yang sesuai
    fun addNewStory(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Header("Authorization") authToken: String
    ): Call<AddStoryResponse>
}