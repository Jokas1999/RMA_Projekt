package com.platuzic.groupfinder.database.dao

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.platuzic.groupfinder.database.models.Post
import java.time.LocalDateTime

class PostDao(
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    fun addPost(post: Post): Task<Void> {
        return database.collection("posts").document(post.id.toString()).set(post)
    }

    fun deletePost(postId: String): Task<Void> {
        return database.collection("posts").document(postId).delete()
    }

    fun getAllGroupPost(groupId: String): Task<QuerySnapshot> {
        return database.collection("posts").whereEqualTo("groupID", groupId).get()
    }

    fun storePostImage(imageByteArray: ByteArray, userId: String): UploadTask {
        return storage.reference.child("posts/$userId${LocalDateTime.now()}").putBytes(imageByteArray)
    }
}