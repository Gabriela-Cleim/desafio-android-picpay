package com.picpay.desafio.android

import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.cache.UserDao
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Call
import retrofit2.Response

class UserRepositoryTest {

    private lateinit var userDao: UserDao
    private lateinit var service: PicPayService
    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        userDao = mock(UserDao::class.java)
        service = mock(PicPayService::class.java)
        repository = UserRepository(service, userDao)
    }

    @Test
    fun fetchAndCacheUsers_success() = runBlocking {
        val users = listOf(User("img", "name", 1, "username"))
        val call = mock(Call::class.java)
        `when`(call.execute()).thenReturn(Response.success(users))
        `when`(service.getUsers()).thenReturn(call as Call<List<User>>?)

        repository.fetchAndCacheUsers()

        verify(userDao).insertUsers(users)
    }

    @Test
    fun getCachedUsers_noUsers() {
        `when`(userDao.getUsers()).thenReturn(emptyList())

        val result = repository.getCachedUsers()

        assertEquals(emptyList<User>(), result)
    }

    @Test
    fun getCachedUsers_withUsers() {
        val users = listOf(User("img", "name", 1, "username"))
        `when`(userDao.getUsers()).thenReturn(users)

        val result = repository.getCachedUsers()

        assertEquals(users, result)
    }
}