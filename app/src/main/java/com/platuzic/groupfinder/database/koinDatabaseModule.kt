package com.platuzic.groupfinder.database

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.platuzic.groupfinder.database.dao.GroupDao
import com.platuzic.groupfinder.database.dao.PostDao
import com.platuzic.groupfinder.database.dao.UserDao
import org.koin.dsl.module

val databaseModule = module {
    single { UserDao(database = FirebaseFirestore.getInstance(), auth = Firebase.auth) }
    single { GroupDao(database = FirebaseFirestore.getInstance()) }
    single { PostDao(database = FirebaseFirestore.getInstance(), storage = FirebaseStorage.getInstance()) }
}

