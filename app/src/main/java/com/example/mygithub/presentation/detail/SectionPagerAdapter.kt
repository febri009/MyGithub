package com.example.mygithub.presentation.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        FollowersFollowingFragment().apply {
            arguments = Bundle().apply {
                putInt(FollowersFollowingFragment.ARG_POSITION, position + 1)
                putString(FollowersFollowingFragment.ARG_USERNAME, username)
            }
        }
}
