package com.platuzic.groupfinder.groupposts.view

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.platuzic.groupfinder.R
import com.platuzic.groupfinder.databinding.FragmentPostBinding
import com.platuzic.groupfinder.groupposts.model.PostRepository
import com.platuzic.groupfinder.groupposts.view.adapters.PostRecyclerAdapter
import com.platuzic.groupfinder.groupposts.viewmodel.GroupsViewModel
import com.platuzic.groupfinder.groupposts.viewmodel.PostViewModel
import lv.chi.photopicker.PhotoPickerFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PostFragment : Fragment(), PhotoPickerFragment.Callback {

    private val viewModel: PostViewModel by sharedViewModel()
    private val groupsViewModel: GroupsViewModel by sharedViewModel()
    private lateinit var binding: FragmentPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner

        val navArgs: PostFragmentArgs by navArgs()

        viewModel.user = groupsViewModel.currentUser.value
        viewModel.group = groupsViewModel.getGroupById(navArgs.groupID)

        binding.FragmentPostAddTextPost.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
        if(viewModel.user?.groups?.contains(navArgs.groupID) == true){
            binding.FragmentPostEnterGroup.visibility = View.GONE
        } else {
            binding.FragmentPostLeaveGroup.visibility = View.GONE
        }

        viewModel.getAllGroupPosts()
        viewModel.posts.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty()){
                binding.FragmentPostRV.adapter = PostRecyclerAdapter(it)
            }
            if(viewModel.addPostImage == null){
                binding.FragmentPostImagePost.setImageResource(R.drawable.ic_baseline_camera_alt_24)
            }
        }

        viewModel.error.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty()){
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onImagesPicked(photos: ArrayList<Uri>) {
        if(photos.isNotEmpty()) {
            viewModel.addPostImage = photos.first()
            Glide.with(requireContext()).load(photos.first()).into(binding.FragmentPostImagePost)
        }
    }

    fun openPicker() {
        PhotoPickerFragment.newInstance(
            multiple = true,
            allowCamera = true,
            maxSelection = 1,
            theme = lv.chi.photopicker.R.style.ChiliPhotoPicker_Light
        ).show(childFragmentManager, "picker")
    }

    fun addTextPostClicked(){
        binding.FragmentPostImagePost.visibility = View.GONE
        binding.FragmentPostEditTextPost.visibility = View.VISIBLE
        binding.FragmentPostAddTextPost.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
        binding.FragmentPostAddImagePost.setColorFilter(ContextCompat.getColor(requireContext(), R.color.brown), android.graphics.PorterDuff.Mode.SRC_IN)
    }

    fun addImagePostClicked(){
        binding.FragmentPostImagePost.visibility = View.VISIBLE
        binding.FragmentPostEditTextPost.visibility = View.GONE
        binding.FragmentPostAddTextPost.setColorFilter(ContextCompat.getColor(requireContext(), R.color.brown), android.graphics.PorterDuff.Mode.SRC_IN)
        binding.FragmentPostAddImagePost.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
    }
}