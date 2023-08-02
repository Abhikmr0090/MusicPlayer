package com.example.payruptask.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ViewModelFactory constructor(private val repository: HomeRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            ContactsViewModel(this.repository) as T
        }
        else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}