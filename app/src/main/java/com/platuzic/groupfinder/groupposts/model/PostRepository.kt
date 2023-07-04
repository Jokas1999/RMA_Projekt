package com.platuzic.groupfinder.groupposts.model

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.UploadTask
import com.platuzic.groupfinder.database.dao.PostDao
import com.platuzic.groupfinder.database.dao.UserDao
import com.platuzic.groupfinder.database.models.Post

class PostRepository(
    private val postDao: PostDao
) {


    fun addPost(post: Post): Task<Void> {
        return postDao.addPost(post = post)
    }

    fun getAllGroupPosts(groupId: String): Task<QuerySnapshot> {
        return postDao.getAllGroupPost(groupId = groupId)
    }

    fun storePostImage(imageByteArray: ByteArray, postID: String): UploadTask {
        return postDao.storePostImage(imageByteArray, postID)
    }

}