package com.platuzic.groupfinder.groupposts.model

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.platuzic.groupfinder.database.dao.GroupDao
import com.platuzic.groupfinder.database.dao.UserDao
import com.platuzic.groupfinder.database.models.Group

class GroupsRepository(
    private val groupDao: GroupDao,
    private val userDao: UserDao
) {

    fun getCurrentUser(): Task<DocumentSnapshot> {
        return userDao.getCurrentUser()
    }

    fun createGroup(name: String, id: String): Task<Void> {
        return groupDao.createGroup(group = Group(id = id, name = name))
    }

    fun leaveGroup(groupId: String): Task<Void> {
        return userDao.leaveGroup(groupId = groupId)
    }

    fun getAllGroups(): Task<QuerySnapshot> {
        return groupDao.getAllGroups()
    }

    fun enterGroup(groupId: String): Task<Void> {
        return userDao.enterGroup(groupId = groupId)
    }
    fun logoutUser() {
        return userDao.logout()
    }
}