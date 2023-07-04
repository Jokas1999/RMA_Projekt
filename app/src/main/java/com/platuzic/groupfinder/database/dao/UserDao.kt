package com.platuzic.groupfinder.database.dao

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.platuzic.groupfinder.database.models.User

class UserDao(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore
) {

    fun login(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }
    fun logout()  {
        auth.signOut();
    }

    fun register(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun storeUser(user: User): Task<Void> {
        return database.collection("users").document(user.id.toString()).set(user)
    }

    fun getCurrentUser(): Task<DocumentSnapshot> {
        return database.collection("users").document(auth.currentUser?.uid.toString()).get()
    }

    fun leaveGroup(groupId: String): Task<Void> {
        return database.collection("users").document(auth.currentUser?.uid.toString()).update("groups", FieldValue.arrayRemove(groupId))
    }

    fun enterGroup(groupId: String): Task<Void> {
        return database.collection("users").document(auth.currentUser?.uid.toString()).update("groups", FieldValue.arrayUnion(groupId))
    }

}