package com.picpay.desafio.android.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.view.viewholder.UserListDiffCallback
import com.picpay.desafio.android.view.viewholder.UserListItemViewHolder
import com.picpay.desafio.android.data.model.User

class UserListAdapter : RecyclerView.Adapter<UserListItemViewHolder>() {

    //
    var users = emptyList<User>()
        set(value) {
            val result = DiffUtil.calculateDiff(
                UserListDiffCallback(
                    field,
                    value
                )
            )
            result.dispatchUpdatesTo(this) // Att adapter
            field = value // Att lista
        }

    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_user, parent, false)

        return UserListItemViewHolder(view) // Novo ViewHolder
    }

    //
    override fun onBindViewHolder(holder: UserListItemViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size
}