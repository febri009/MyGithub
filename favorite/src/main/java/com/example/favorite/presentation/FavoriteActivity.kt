package com.example.favorite.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.domain.model.Users
import com.example.core.ui.UserAdapter
import com.example.favorite.databinding.ActivityFavoriteBinding
import com.example.favorite.di.favoriteModule
import com.example.mygithub.presentation.detail.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter

    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadKoinModules(favoriteModule)

        observeFavoriteUsers()
        setupRecyclerView()
    }

    private fun observeFavoriteUsers() {
        favoriteViewModel.observeUserFavorite().observe(this) { users ->
            updateUIBasedOnFavoriteUsers(users)
        }
    }

    private fun updateUIBasedOnFavoriteUsers(users: List<Users>) {
        if (users.isNullOrEmpty()) {
            adapter.submitList(null)
            showEmptyState(true)
            adapter.submitList(null)
        } else {
            showEmptyState(false)
            adapter.submitList(users)
            adapter.onItemClick = { selectedData ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(DetailActivity.USER, selectedData.login)
                startActivity(intent)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter()
        val layoutManager = LinearLayoutManager(this@FavoriteActivity)
        layoutManager.isItemPrefetchEnabled = true

        binding.rvUser.layoutManager = layoutManager
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter
    }


    private fun showEmptyState(isEmpty: Boolean) {
        binding.empty.root.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}