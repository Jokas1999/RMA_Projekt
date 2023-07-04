package com.platuzic.groupfinder.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platuzic.groupfinder.auth.model.AuthRepository

class LoginViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()
    val error = MutableLiveData("")


    fun login() {
        try {
            if (email.value?.isNotBlank() == true && password.value?.isNotBlank() == true) {
                authRepository.loginUser(
                    email = email.value.toString(),
                    password = password.value.toString()
                ).addOnCompleteListener { loginTask ->
                    if (loginTask.isSuccessful) {
                        success.postValue(true)
                    } else {
                        error.postValue("Login did not succeed! Check your credentials!")
                    }
                }

            } else {
                error.postValue("Fill all the required fields first")
            }
        } catch (e: Exception) {
            error.postValue("Something went wrong")
        }
    }
}


