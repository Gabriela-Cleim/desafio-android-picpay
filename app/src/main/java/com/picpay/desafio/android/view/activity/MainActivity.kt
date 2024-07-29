package com.picpay.desafio.android.view.activity

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

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    // Inicialização do adapter
    private lateinit var adapter: UserListAdapter

    // Declaração da variável de binding
    private lateinit var binding: ActivityMainBinding



    private val url = "https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/"

    // Inicialização do serviço de API usando Retrofit
    private val service: PicPayService by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(PicPayService::class.java)
    }

    // Inicialização do banco de dados Room
    private val database: AppDatabase by lazy {  // Usa lazy para inicializar o banco de dados quando necessário.
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "picpay-database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    // Inicialização do ViewModel
    private val viewModel: UserViewModel by viewModels {  // Usa viewModels para obter uma instância do ViewModel.
        UserViewModelFactory(UserRepository(service, database.userDao()), this)  // Passa a fábrica que cria uma instância do ViewModel.
    }

    override fun onResume() {
        super.onResume()

        // Inicializa o binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuração do RecyclerView
        adapter = UserListAdapter()  // Cria uma nova instância do adapter.
        binding.recyclerView.adapter = adapter  // Define o adapter para a RecyclerView.
        binding.recyclerView.layoutManager = LinearLayoutManager(this)



        // Observadores para o LiveData no ViewModel
        viewModel.users.observe(this, Observer {  // Observa mudanças na lista de usuários do ViewModel.
            adapter.users = it  // Atualiza os dados do adapter com a lista de usuários.
        })

        viewModel.loading.observe(this, Observer {  // Observa mudanças no estado de carregamento do ViewModel.
            binding.userListProgressBar.visibility = if (it) View.VISIBLE else View.GONE  // Mostra ou esconde o indicador de carregamento.
        })

        viewModel.error.observe(this, Observer {  // Observa mudanças nas mensagens de erro do ViewModel.
            it?.let { message ->  // Se houver uma mensagem de erro.
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()  // Exibe a mensagem de erro em um Toast.
            }
        })

        // Buscar usuários ao criar a atividade
        viewModel.fetchUsers()  // Chama o método do ViewModel para buscar os usuários.


    }
}
