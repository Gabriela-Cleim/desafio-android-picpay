package com.picpay.desafio.android.view.viewholder

import androidx.recyclerview.widget.DiffUtil
import com.picpay.desafio.android.data.model.User

class UserListDiffCallback(
    private val oldList: List<User>,
    private val newList: List<User>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].username.equals(newList[newItemPosition].username)
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]

        return oldUser.id == newUser.id &&
                oldUser.name == newUser.name &&
                oldUser.username == newUser.username &&
                oldUser.img == newUser.img
    }
}