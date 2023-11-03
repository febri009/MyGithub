package com.example.mygithub.presentation.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.core.data.Resource
import com.example.core.domain.model.Users
import com.example.mygithub.R
import com.example.mygithub.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(USER)

        if (username != null) {
            supportActionBar?.title = username
            showDetailContent(username)
            initializeViewPager(username)
        }
    }

    private fun initializeViewPager(username: String) {
        val sectionPagerAdapter = SectionPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = getString(TAB_TITLES[position])
        }.attach()
    }

    private fun showDetailContent(username: String) {
        detailViewModel.fetchUserDetail(username).observe(this) { users ->
            when (users) {
                is Resource.Loading -> {
                    handleLoadingState(true)
                }
                is Resource.Success -> {
                    handleLoadingState(false)
                    if (users.data != null) {
                        handleDataState(users.data!!)
                    } else {
                        handleEmptyState(true)
                    }
                }
                is Resource.Error -> {
                    handleLoadingState(false)
                    handleErrorState(true)
                }
            }
        }
    }

    private fun handleDataState(data: Users) {
        binding.apply {
            Glide.with(applicationContext)
                .load(data.avatarUrl)
                .into(ivPhoto)

            tvUsername.text = data.login
            tvName.text = data.name
            tabs.getTabAt(0)?.text = getString(R.string.followers, data.followers)
            tabs.getTabAt(1)?.text = getString(R.string.following, data.following)
            handleFavoriteStatus(data)
        }
        handleEmptyState(false)
        handleErrorState(false)
    }

    private fun handleFavoriteStatus(users: Users) {
        detailViewModel.checkFavoriteIsExists(users.login).observe(this) { isFavorite ->
            val drawableRes = if (isFavorite) R.drawable.ic_star_24 else R.drawable.ic_star_outline_24
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, drawableRes))

            binding.fabFavorite.setOnClickListener {
                val messageRes = if (isFavorite) R.string.delete_favorite else R.string.add_favorite
                showToast(getString(messageRes))
                if (isFavorite) {
                    detailViewModel.deleteUserFavorite(users)
                } else {
                    detailViewModel.addUserFavorite(users)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun handleLoadingState(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun handleEmptyState(isEmpty: Boolean) {
        binding.empty.root.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun handleErrorState(isError: Boolean) {
        binding.apply {
            if (isError) {
                error.root.visibility = View.VISIBLE
            } else {
                error.root.visibility = View.GONE
            }
        }
    }

    companion object {
        const val USER = "user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers, R.string.following
        )
    }
}
