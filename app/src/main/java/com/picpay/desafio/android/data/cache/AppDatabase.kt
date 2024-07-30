package com.picpay.desafio.android.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.picpay.desafio.android.data.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}