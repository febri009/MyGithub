package com.example.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.databinding.ItemUserBinding
import com.example.core.domain.model.Users


class UserAdapter : ListAdapter<Users, UserAdapter.ListViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((Users) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class ListViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(users: Users) {
            loadUserData(users)
            setOnClickListener(users)
        }

        private fun loadUserData(users: Users) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(users.avatarUrl)
                    .into(ivUser)

                tvUsername.text = users.login
            }
        }

        private fun setOnClickListener(users: Users) {
            itemView.setOnClickListener {
                onItemClick?.invoke(users)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Users>() {
            override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
                return oldItem.login == newItem.login
            }

        }
    }
}