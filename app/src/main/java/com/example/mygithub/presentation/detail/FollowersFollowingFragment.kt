package com.example.mygithub.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.data.Resource
import com.example.core.ui.UserAdapter
import com.example.mygithub.databinding.FragmentFollowersFollowingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FollowersFollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowersFollowingBinding
    private lateinit var adapter: UserAdapter
    private val detailViewModel: DetailViewModel by viewModel()

    private var position = 0
    private var username = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFollowersFollowingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        arguments?.let {
            position = it.getInt(ARG_POSITION, 0)
            username = it.getString(ARG_USERNAME).toString()
        }

        observeUserData()
    }

    private fun observeUserData() {
        val dataLiveData = if (position == 1) {
            detailViewModel.fetchUserFollowers(username)
        } else {
            detailViewModel.fetchUserFollowing(username)
        }

        dataLiveData.observe(viewLifecycleOwner) { dataResource ->
            when (dataResource) {
                is Resource.Loading -> {
                    loadingState(true)
                }
                is Resource.Success -> {
                    loadingState(false)
                    if (dataResource.data.isNullOrEmpty()) {
                        emptyState(true)
                    } else {
                        emptyState(false)
                        adapter.submitList(dataResource.data)
                    }
                }
                is Resource.Error -> {
                    loadingState(false)
                    errorState(true)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.rvUser.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onPause() {
        super.onPause()
        binding.rvUser.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0
        )
    }

    private fun setAdapter() {
        adapter = UserAdapter()

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(requireActivity())
            rvUser.setHasFixedSize(false)
            rvUser.adapter = adapter
        }
    }

    private fun loadingState(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun emptyState(isEmpty: Boolean) {
        binding.empty.root.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun errorState(isError: Boolean) {
        binding.error.root.visibility = if (isError) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = "section_username"
    }
}

