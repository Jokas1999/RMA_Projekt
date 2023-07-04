package com.platuzic.groupfinder.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platuzic.groupfinder.auth.model.AuthRepository
import com.platuzic.groupfinder.database.models.User
import java.util.*

class RegisterViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val repeatPassword = MutableLiveData<String>()

    val success = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun register() {
        try {
            if(checkInputs()){
                authRepository.registerUser(email = email.value.toString(), password = password.value.toString())
                    .addOnCompleteListener { registerTask ->
                        if(registerTask.isSuccessful){
                            authRepository.storeUser(
                                User(
                                    id = registerTask.result.user?.uid.toString(),
                                    name = name.value.toString(),
                                    password = password.value.toString(),
                                    email = email.value.toString(),
                                    groups = emptyList()
                                )
                            )
                                .addOnCompleteListener{ storeUserTask ->
                                    if(storeUserTask.isSuccessful){
                                        success.postValue(true)
                                    } else {
                                        storeUserTask.exception?.printStackTrace()
                                        error.postValue("A problem occurred with the registration. Check your inputs and try again later!")
                                    }
                                }
                        } else {
                            registerTask.exception?.printStackTrace()
                            error.postValue("A problem occurred with the registration. Check your inputs and try again later!")
                        }
                    }
            } else {
                error.postValue("A problem occurred with the registration. Check your inputs and try again later!")
            }
        } catch (e: Exception){
            error.postValue("A problem occurred with the registration. Check your inputs and try again later!")
            e.printStackTrace()
        }
    }

    private fun checkInputs(): Boolean{
        if(password.value !=  repeatPassword.value)
            return false

        if(email.value?.isNotBlank() == false)
            return false

        if(name.value?.isNotBlank() == false)
            return false

        if(password.value?.isNotBlank() == false)
            return false

        if(repeatPassword.value?.isNotBlank() == false)
            return false


        return true
    }
}