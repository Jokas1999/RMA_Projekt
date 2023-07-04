package com.platuzic.groupfinder.database.dao

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.platuzic.groupfinder.database.models.Group

class GroupDao(
    private val database: FirebaseFirestore,
) {
    fun createGroup(group: Group): Task<Void> {
        return database.collection("groups").document(group.id.toString()).set(group)
    }

    fun getAllGroups(): Task<QuerySnapshot> {
        return database.collection("groups").get()
    }

    fun getGroup(groupId: String): Task<DocumentSnapshot> {
        return database.collection("groups").document(groupId).get()
    }
}