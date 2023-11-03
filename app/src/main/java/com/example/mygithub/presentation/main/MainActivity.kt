package com.example.mygithub.presentation.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.data.Resource
import com.example.core.domain.model.Users
import com.example.mygithub.R
import com.example.core.ui.UserAdapter
import com.example.mygithub.databinding.ActivityMainBinding
import com.example.mygithub.presentation.detail.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private val mainViewModel: MainViewModel by viewModel()
    private var isDarkMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews(){
        stateEmpty(true)
        setupTheme()
        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = getString(R.string.search_usn)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    showData(query.toString())
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }

            })
        }

        val themeChanger = menu.findItem(R.id.theme_changer)

        themeChanger.setIcon(
            if (isDarkMode) {
                R.drawable.ic_outline_lightbulb_24
            }
            else {
                R.drawable.ic_lightbulb_24
            }
        )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                val uri = Uri.parse("githubuser://favorite")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }

            R.id.theme_changer -> {
                val message = if (isDarkMode) getString(R.string.light_theme)
                else getString(R.string.dark_theme)

                showToast(message)

                val newMode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_NO
                else AppCompatDelegate.MODE_NIGHT_YES

                AppCompatDelegate.setDefaultNightMode(newMode)
                mainViewModel.saveAppThemeSetting(!isDarkMode)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupTheme() {
        mainViewModel.fetchAppThemeSetting().observe(this) { isDarkModeActive ->
            isDarkMode = isDarkModeActive
            val mode = if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter()

        binding.listUser.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }
    }

    private fun showData(username: String) {
        mainViewModel.fetchUsersByUsername(username).observe(this) { users ->
            handleUserData(users)
        }
    }

    private fun handleUserData(users: Resource<List<Users>>) {
        when (users) {
            is Resource.Loading -> {
                setState(isLoading = true, isEmpty = false, isError = false)
            }

            is Resource.Success -> {
                setState(isLoading = false, isEmpty = users.data.isNullOrEmpty(), isError = false)
                if (!users.data.isNullOrEmpty()) {
                    adapter.submitList(users.data)
                    adapter.onItemClick = { selectedData ->
                        openUserDetail(selectedData)
                    }
                }
            }

            is Resource.Error -> {
                setState(isLoading = false, isEmpty = false, isError = true)
            }

            else -> {}
        }
    }

    private fun openUserDetail(selectedData: Users) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.USER, selectedData.login)
        startActivity(intent)
    }

    private fun setState(isLoading: Boolean, isEmpty: Boolean, isError: Boolean) {
        stateLoading(isLoading)
        stateEmpty(isEmpty)
        stateError(isError)
    }


    private fun View.showIf(condition: Boolean) {
        visibility = if (condition) View.VISIBLE else View.GONE
    }

    private fun stateLoading(isLoading: Boolean) {
        binding.listUser.progressBar.showIf(isLoading)
    }

    private fun stateEmpty(isEmpty: Boolean) {
        binding.empty.root.showIf(isEmpty)
    }

    private fun stateError(isError: Boolean) {
        binding.error.root.showIf(isError)
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

}

