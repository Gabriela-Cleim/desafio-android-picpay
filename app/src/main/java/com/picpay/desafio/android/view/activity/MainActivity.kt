package com.picpay.desafio.android.view.activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.R
import com.picpay.desafio.android.view.adapter.UserListAdapter
import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.cache.AppDatabase
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.databinding.ActivityMainBinding
import com.picpay.desafio.android.viewmodel.UserViewModel
import com.picpay.desafio.android.viewmodel.UserViewModelFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var adapter: UserListAdapter

    private lateinit var binding: ActivityMainBinding

    private val url = "https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/"

    //
    private val service: PicPayService by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(PicPayService::class.java)
    }

    //
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "picpay-database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(service, database.userDao()), this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) //

        adapter = UserListAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.users.observe(this, Observer {
            adapter.users = it
        })

        viewModel.loading.observe(this, Observer {
            binding.userListProgressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.error.observe(this, Observer {
            it?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.fetchUsers() //
    }

}
