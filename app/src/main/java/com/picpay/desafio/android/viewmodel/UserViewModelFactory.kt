package com.picpay.desafio.android.viewmodel

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.picpay.desafio.android.data.repository.UserRepository

//
class UserViewModelFactory(private val repository: UserRepository, owner: SavedStateRegistryOwner, defaultArgs: Bundle? = null) : AbstractSavedStateViewModelFactory(owner, defaultArgs)  {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            UserViewModel(repository, handle) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}