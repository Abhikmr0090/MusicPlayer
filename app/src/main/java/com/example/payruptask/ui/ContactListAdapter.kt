package com.example.payruptask.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.payruptask.databinding.ContactListItemBinding
import com.example.payruptask.network.UsersModel

class ContactListAdapter(
    private var data: ArrayList<UsersModel>,
    private val listener: (UsersModel) -> Unit
) : ListAdapter<UsersModel, ContactListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ContactListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(usersModel: UsersModel) {
            binding.name.text = usersModel.name
            binding.email.text = usersModel.email

            binding.root.setOnClickListener {
                listener.invoke(usersModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ContactListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun initList(sortedList: ArrayList<UsersModel>) {
        data = sortedList
        notifyDataSetChanged()
    }
}

val diffUtil = object : DiffUtil.ItemCallback<UsersModel>() {
    override fun areItemsTheSame(oldItem: UsersModel, newItem: UsersModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UsersModel, newItem: UsersModel): Boolean {
        return oldItem == newItem
    }
}