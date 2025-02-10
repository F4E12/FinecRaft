package com.example.qualif.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qualif.databinding.ItemUserBinding
import com.example.qualif.model.User

class UserAdapter(private val userList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]

        holder.binding.tvUserId.text = "ID: ${user.id}"
        holder.binding.tvUserName.text = "Name: ${user.name}"
        holder.binding.tvUserEmail.text = "Email: ${user.email}"
        holder.binding.tvUserPhone.text = "Phone: ${user.phone}"
    }

    override fun getItemCount(): Int = userList.size
}
