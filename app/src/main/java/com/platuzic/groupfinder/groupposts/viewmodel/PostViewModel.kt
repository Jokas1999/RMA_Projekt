package com.platuzic.groupfinder.groupposts.viewmodel

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platuzic.groupfinder.GroupFinderApplication
import com.platuzic.groupfinder.database.models.Group
import com.platuzic.groupfinder.database.models.Post
import com.platuzic.groupfinder.database.models.User
import com.platuzic.groupfinder.groupposts.model.GroupsRepository
import com.platuzic.groupfinder.groupposts.model.PostRepository
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class PostViewModel(
    private val postRepository: PostRepository,
    private val groupsRepository: GroupsRepository
) : ViewModel() {

    var user: User? = null
    var group: Group? = null

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    val error = MutableLiveData<String>()

    var addPostImage: Uri? = null
    val addPostText = MutableLiveData<String>()

    fun getAllGroupPosts() {
        try {
            postRepository.getAllGroupPosts(groupId = group?.id.toString())
                .addOnCompleteListener { getPostsTask ->
                    if (getPostsTask.isSuccessful) {
                        _posts.postValue(getPostsTask.result.toObjects(Post::class.java))
                    } else {
                        getPostsTask.exception?.printStackTrace()
                        error.postValue("Couldn't load group posts. Please try again!")
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            error.postValue("Something went wrong! Please try again.")
        }
    }

    fun enterGroup() {
        try {
            groupsRepository.enterGroup(group?.id.toString())
                .addOnCompleteListener { enterGroupTask ->
                    if (!enterGroupTask.isSuccessful) {
                        enterGroupTask.exception?.printStackTrace()
                        error.postValue("An error occurred! Please try again!")
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            error.postValue("An error occurred! Please try again!")
        }
    }

    fun createPost() {
        try {
            if (!addPostText.value.isNullOrEmpty()) {
                val post = Post(
                    id = UUID.randomUUID().toString(),
                    text = addPostText.value.toString(),
                    groupID = group?.id,
                    userName = user?.name
                )
                addPost(post = post)
            } else {
                val postID = UUID.randomUUID().toString()
                val imageByteArray = addPostImage?.let { getImageByteArray(it) }
                if (imageByteArray != null) {
                    postRepository.storePostImage(imageByteArray, postID = postID)
                        .addOnCompleteListener { uploadTask ->
                            if (uploadTask.isSuccessful) {
                                uploadTask.result.storage.downloadUrl
                                    .addOnCompleteListener { downloadUrlTask ->
                                        if(downloadUrlTask.isSuccessful) {
                                            addPost(
                                                Post(
                                                    id = postID,
                                                    image = downloadUrlTask.result.toString(),
                                                    groupID = group?.id,
                                                    userName = user?.name
                                                )
                                            )
                                        } else {
                                            downloadUrlTask.exception?.printStackTrace()
                                            error.postValue("Something went wrong with creating your post. Please try again!")
                                        }
                                    }
                            } else {
                                uploadTask.exception?.printStackTrace()
                                error.postValue("Something went wrong with creating your post. Please try again!")
                            }
                        }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            error.postValue("Something went wrong with creating your post. Please try again!")
        }
    }

    private fun addPost(post: Post) {
        try {
            postRepository.addPost(post)
                .addOnCompleteListener { addPostTask ->
                    if (addPostTask.isSuccessful) {
                        val postsTemp = mutableListOf<Post>()
                        postsTemp.addAll(posts.value ?: emptyList())
                        postsTemp.add(0, post)
                        _posts.postValue(postsTemp)

                        addPostImage = null
                        addPostText.postValue("")
                    } else {
                        addPostTask.exception?.printStackTrace()
                        error.postValue("Something went wrong with creating your post. Please try again!")
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            error.postValue("Something went wrong with creating your post. Please try again!")
        }
    }


    private fun getImageByteArray(file: Uri): ByteArray {
        var bitmap: Bitmap? = null
        try {
            val inputStream =
                GroupFinderApplication.getAppContext().contentResolver.openInputStream(file)
            bitmap = BitmapFactory.decodeStream(inputStream)

            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        return data
    }

    fun leaveGroup(){
        try {
            groupsRepository.leaveGroup(group?.id.toString())
                .addOnCompleteListener { leaveGroupTask ->
                    if(!leaveGroupTask.isSuccessful){
                        leaveGroupTask.exception?.printStackTrace()
                        error.postValue("An error occurred! Please try again!")
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            error.postValue("An error occurred! Please try again!")
        }
    }
}