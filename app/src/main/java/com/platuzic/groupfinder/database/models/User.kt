package com.platuzic.groupfinder.database.models

data class User(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val groups: List<String> = emptyList()
)
