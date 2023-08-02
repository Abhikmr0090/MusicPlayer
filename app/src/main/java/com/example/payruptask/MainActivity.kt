package com.example.payruptask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.payruptask.databinding.ActivityMainBinding
import com.example.payruptask.network.ApiHandler
import com.example.payruptask.ui.ContactsViewModel
import com.example.payruptask.ui.HomeRepository
import com.example.payruptask.ui.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding    : ActivityMainBinding
    private lateinit var repository : HomeRepository
    private lateinit var viewModel  : ContactsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding     = DataBindingUtil.setContentView(this, R.layout.activity_main)
        repository  = HomeRepository(ApiHandler.retrofit)
        viewModel   = ViewModelProvider(this, ViewModelFactory(repository))[ContactsViewModel::class.java]
    }
}