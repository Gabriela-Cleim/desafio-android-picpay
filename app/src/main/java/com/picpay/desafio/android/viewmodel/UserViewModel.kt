package com.picpay.desafio.android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val repository: UserRepository, private val savedStateHandle: SavedStateHandle) : ViewModel(){

    val users = MutableLiveData<List<User>>()

    val loading = MutableLiveData<Boolean>()

    val error = MutableLiveData<String>()

    //
    fun fetchUsers() {
        loading.value = true
        viewModelScope.launch {
            try {
                val cachedUsers = withContext(Dispatchers.IO) {//
                    repository.getCachedUsers()
                }

                if (cachedUsers.isNotEmpty()) {
                    users.postValue(cachedUsers)
                    loading.postValue(false)
                }else{
                    repository.fetchAndCacheUsers()
                    val updatedUsers = withContext(Dispatchers.IO) {
                        repository.getCachedUsers()
                    }
                    users.postValue(updatedUsers)
                    loading.postValue(false)
                }
            } catch (e: Exception) {
                error.postValue(e.message)
                loading.postValue(false)
            }
        }
    }
}