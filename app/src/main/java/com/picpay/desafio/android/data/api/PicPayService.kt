package com.picpay.desafio.android.data.api

import com.picpay.desafio.android.data.model.User
import retrofit2.Call
import retrofit2.http.GET

// Define endpoint da API.
interface PicPayService {

    // Obtem a lista de usuários.
    @GET("users")
    fun getUsers(): Call<List<User>>
}