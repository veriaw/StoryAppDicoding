package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.RegisterResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response

class SignUpViewModel: ViewModel() {
    private val _register = MutableLiveData<RegisterResponse>()
    val register: LiveData<RegisterResponse> = _register
    fun registerUser(name: String, email: String, password: String){
        val jsonObject = JsonObject().apply {
            addProperty("name", name)
            addProperty("email", email)
            addProperty("password", password)
        }
        val client = ApiConfig.getApiService().registerUser(jsonObject)
        client.enqueue(object : retrofit2.Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    _register.value = response.body()
                    Log.e("Register Auth", "Succesfully Registered")
                }else{
                    Log.e("Register Auth", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("Register Auth", "onFailure: ${t.message.toString()}")
            }

        })
    }
}