package com.picpay.desafio.android.data.repository

import android.util.Log
import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.cache.UserDao
import com.picpay.desafio.android.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

class UserRepository(private val service: PicPayService, private val userDao: UserDao){

    fun getUsers(): Call<List<User>> {
        return service.getUsers()
    }

    //
    suspend fun fetchAndCacheUsers() {
        withContext(Dispatchers.IO) {

            val response = service.getUsers().execute()

            if(response.isSuccessful) {
                response.body()?.let { users ->
                    userDao.insertUsers(users)
                }
            }else{
                Log.e("UserRepository", "Failed to fetch users: ${response.errorBody()?.string()}")
            }
        }
    }

    fun getCachedUsers(): List<User> {
        return userDao.getUsers()
    }
}