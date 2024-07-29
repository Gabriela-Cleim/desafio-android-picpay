package com.picpay.desafio.android.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.databinding.ListItemUserBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class UserListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // Inicializa o binding usando o método bind
    private val binding = ListItemUserBinding.bind(itemView)

    // Liga os dados do usuário às views
    fun bind(user: User) {

        binding.name.text = user.name
        binding.username.text = user.username
        binding.progressBar.visibility = View.VISIBLE

        Picasso.get()
            .load(user.img)
            .error(R.drawable.ic_round_account_circle)
            .into(binding.picture, object : Callback {

                override fun onSuccess() {
                    binding.progressBar.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    binding.progressBar.visibility = View.GONE
                }
            })
    }
}