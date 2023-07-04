package com.platuzic.groupfinder.auth.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import com.platuzic.groupfinder.R
import com.platuzic.groupfinder.auth.viewmodel.RegisterViewModel
import com.platuzic.groupfinder.databinding.FragmentRegisterBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by sharedViewModel()
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.error.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty()){
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.error.postValue("")
            }
        }

        viewModel.success.observe(viewLifecycleOwner){
            if(it)
                findNavController().navigate(R.id.action_registerFragment_to_groups_navigation)
        }
        return binding.root
    }

}