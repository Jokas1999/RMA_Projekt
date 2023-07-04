package com.platuzic.groupfinder.groupposts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platuzic.groupfinder.database.models.Group
import com.platuzic.groupfinder.database.models.User
import com.platuzic.groupfinder.groupposts.model.GroupsRepository
import java.util.*

class GroupsViewModel(
    private val groupsRepository: GroupsRepository
) : ViewModel() {

    val success = MutableLiveData<Boolean>()


    private val _allJoinedGroups = MutableLiveData<List<Group>>()
    val allJoinedGroups: LiveData<List<Group>> get() = _allJoinedGroups

    private val _notJoinedGroups = MutableLiveData<List<Group>>()
    val notJoinedGroups: LiveData<List<Group>> get() = _notJoinedGroups

    private var allJoinedGroupsCache: List<Group> = emptyList()
    private var notJoinedGroupsCache: List<Group> = emptyList()

    val currentUser = MutableLiveData<User>()
    val error = MutableLiveData<String>()

    val searchText = MutableLiveData<String>()
    val groupName = MutableLiveData<String>()

    fun getCurrentUser() {
        try {
            groupsRepository.getCurrentUser()
                .addOnCompleteListener { currentUserTask ->
                    if (currentUserTask.isSuccessful) {
                        currentUser.postValue(currentUserTask.result.toObject(User::class.java))
                    } else {
                        currentUserTask.exception?.printStackTrace()
                        error.postValue("Something went wrong! Please try again.")
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            error.postValue("Something went wrong! Please try again.")
        }
    }

    fun getAllGroups() {
        try {
            groupsRepository.getAllGroups()
                .addOnCompleteListener { allGroupsTask ->
                    if (allGroupsTask.isSuccessful) {
                        filterGroups(allGroupsTask.result.toObjects(Group::class.java))
                    } else {
                        allGroupsTask.exception?.printStackTrace()
                        error.postValue("Something went wrong! Please try again.")
                    }
                }
        } catch (e: Exception){
            error.postValue("Something went wrong! Please try again.")
            e.printStackTrace()
        }
    }

    private fun filterGroups(groups: List<Group>){
        val userGroups = currentUser.value?.groups
        val notJoinedGroups = groups.filterNot { group ->
            userGroups?.any{ userGroup ->
                group.id == userGroup
            } == true
        }
        val joinedGroups = groups.filter { group ->
            userGroups?.any { userGroup ->
                group.id == userGroup
            } == true
        }
        notJoinedGroupsCache = notJoinedGroups
        allJoinedGroupsCache = joinedGroups
        _notJoinedGroups.postValue(notJoinedGroups)
        _allJoinedGroups.postValue(joinedGroups)
    }

    fun searchTextQuery(){
        if(searchText.value?.isNullOrEmpty() == true){
            getAllGroups()
        } else {
            _allJoinedGroups.postValue(
                allJoinedGroupsCache.filter { group ->
                    group.name?.lowercase()?.startsWith(searchText.value.toString().lowercase()) == true
                }
            )
            _notJoinedGroups.postValue(
                notJoinedGroupsCache.filter { group ->
                    group.name?.lowercase()?.startsWith(searchText.value.toString().lowercase()) == true
                }
            )
        }
    }

    fun addGroup(){
        try {
            if(groupName.value?.isNotBlank() == true) {
                val groupId = UUID.randomUUID().toString()
                groupsRepository.createGroup(groupName.value.toString(), groupId)
                    .addOnCompleteListener { addGroupTask ->
                        if (addGroupTask.isSuccessful) {
                            getAllGroups()
                        } else {
                            addGroupTask.exception?.printStackTrace()
                            error.postValue("Couldn't add group. Something went wrong!. Please try again.")
                        }
                    }
            } else {
                error.postValue("Please provide group name")
            }
        } catch (e: Exception){
            e.printStackTrace()
            error.postValue("Couldn't add group. Something went wrong!. Please try again.")
        }
    }

    fun getGroupById(groupID: String): Group? {
        val joinedGroup = allJoinedGroups.value?.filter { group ->
            group.id == groupID
        }
        return if(!joinedGroup.isNullOrEmpty()){
            joinedGroup.first()
        } else {
            val notJoinedGroup = notJoinedGroups.value?.filter { group ->
                group.id == groupID
            }
            if(!notJoinedGroup.isNullOrEmpty()){
                notJoinedGroup.first()
            } else {
                null
            }
        }
    }

    fun logout(){
                groupsRepository.logoutUser()
                success.postValue(true)


    }
}