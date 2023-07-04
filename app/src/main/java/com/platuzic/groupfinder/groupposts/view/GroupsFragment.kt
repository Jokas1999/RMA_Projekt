package com.platuzic.groupfinder.groupposts.view

import android.app.ActionBar
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.platuzic.groupfinder.R
import com.platuzic.groupfinder.database.models.Group
import com.platuzic.groupfinder.databinding.DialogAddGroupBinding
import com.platuzic.groupfinder.databinding.FragmentGroupsBinding
import com.platuzic.groupfinder.groupposts.view.adapters.GroupsRecyclerAdapter
import com.platuzic.groupfinder.groupposts.viewmodel.GroupsViewModel
import com.platuzic.groupfinder.utils.OnItemClick
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class GroupsFragment : Fragment() {

    private val viewModel: GroupsViewModel by sharedViewModel()
    private lateinit var binding: FragmentGroupsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.getCurrentUser()
        viewModel.currentUser.observe(viewLifecycleOwner){
            if(it != null) {
                if (it.id.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.getAllGroups()
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty()){
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.error.postValue("")
            }
        }

        viewModel.allJoinedGroups.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty()){
                binding.FragmentGroupsJoinedGroupsRV.visibility = View.VISIBLE
                binding.FragmentGroupsJoinedGroupsTitle.visibility = View.VISIBLE
                binding.FragmentGroupsJoinedGroupsRV.adapter = GroupsRecyclerAdapter(it, itemClickListener)
            } else {
                binding.FragmentGroupsJoinedGroupsRV.visibility = View.GONE
                binding.FragmentGroupsJoinedGroupsTitle.visibility = View.GONE
            }
        }

        viewModel.notJoinedGroups.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty()){
                binding.FragmentGroupsNotJoinedGroupsRV.visibility = View.VISIBLE
                binding.FragmentGroupsNotJoinedGroupsTitle.visibility = View.VISIBLE
                binding.FragmentGroupsNotJoinedGroupsRV.adapter = GroupsRecyclerAdapter(it, itemClickListener)
            } else {
                binding.FragmentGroupsNotJoinedGroupsRV.visibility = View.GONE
                binding.FragmentGroupsNotJoinedGroupsTitle.visibility = View.GONE
            }
        }
        viewModel.success.observe(viewLifecycleOwner){
            if(it)
                findNavController().navigate(R.id.action_groupsFragment_to_postFragment)
        }
        return binding.root
    }

    fun addGroup() {
        val binding = DialogAddGroupBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(requireContext()).apply {
            setContentView(binding.root)
            setCancelable(true)
            show()
        }
        val window: Window? = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.viewModel = viewModel

        binding.DialogAddGroupAdd.setOnClickListener {
            viewModel.addGroup()
            dialog.dismiss()
        }

        binding.DialogAddGroupClose.setOnClickListener {
            dialog.dismiss()
        }
    }

    private val itemClickListener = object: OnItemClick{
        override fun onClick(item: Any?) {
            item as Group
            findNavController().navigate(GroupsFragmentDirections.actionGroupsFragmentToPostFragment(
                item.id.toString()
            ))
        }
    }
}