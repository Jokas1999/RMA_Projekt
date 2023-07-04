package com.platuzic.groupfinder.auth.model

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.platuzic.groupfinder.database.dao.UserDao
import com.platuzic.groupfinder.database.models.User

class AuthRepository(
    private val userDao: UserDao
) {

    fun loginUser(email: String, password: String): Task<AuthResult> {
        return userDao.login(email = email, password = password)
    }

    fun registerUser(email: String, password: String): Task<AuthResult> {
        return userDao.register(email = email, password = password)
    }

    fun storeUser(user: User): Task<Void> {
        return userDao.storeUser(user = user)
    }
}