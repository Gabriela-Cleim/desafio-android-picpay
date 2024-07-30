package com.picpay.desafio.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class UserViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private val repository = Mockito.mock(UserRepository::class.java)
    private lateinit var viewModel: UserViewModel

    private val usersObserver = Mockito.mock(Observer::class.java) as Observer<List<User>>  // Cria um mock para o observador de usuários.
    private val loadingObserver = Mockito.mock(Observer::class.java) as Observer<Boolean>
    private val errorObserver = Mockito.mock(Observer::class.java) as Observer<String>

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val savedStateHandle = SavedStateHandle()

        viewModel = UserViewModel(repository, savedStateHandle)

        viewModel.users.observeForever(usersObserver)
        viewModel.loading.observeForever(loadingObserver)
        viewModel.error.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testFetchUsers() = runBlocking {
        val users = listOf(User("img", "name", 1, "username"))
        Mockito.`when`(repository.getCachedUsers()).thenReturn(users)

        viewModel.fetchUsers()

        Mockito.verify(loadingObserver).onChanged(true)
        Mockito.verify(usersObserver).onChanged(users)
        Mockito.verify(loadingObserver).onChanged(false)
    }
}
