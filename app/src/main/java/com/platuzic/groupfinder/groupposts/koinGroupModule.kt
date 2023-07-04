package com.platuzic.groupfinder.groupposts

import com.platuzic.groupfinder.groupposts.model.GroupsRepository
import com.platuzic.groupfinder.groupposts.model.PostRepository
import com.platuzic.groupfinder.groupposts.viewmodel.GroupsViewModel
import com.platuzic.groupfinder.groupposts.viewmodel.PostViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val groupModule = module {
    single { GroupsRepository(groupDao = get(), userDao = get()) }
    viewModel{ GroupsViewModel(groupsRepository = get()) }
    single { PostRepository(postDao = get()) }
    viewModel { PostViewModel(postRepository = get(), groupsRepository = get()) }
}