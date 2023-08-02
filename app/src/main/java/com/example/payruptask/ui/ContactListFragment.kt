package com.example.payruptask.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.payruptask.R
import com.example.payruptask.databinding.FragmentContactListBinding
import com.example.payruptask.network.NetworkStates
import com.example.payruptask.network.UsersModel

class ContactListFragment : Fragment() {

    private lateinit var binding    : FragmentContactListBinding
    private val viewModel           : ContactsViewModel by activityViewModels()
    private lateinit var adapter    : ContactListAdapter
    private var responseData = ArrayList<UsersModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_list, container, false)
        setObserver()
        setMenuBar()

        if (viewModel.contactsList.value == null || viewModel.contactsList.value?.isEmpty() == true) {
            getContactsData()
        }

        return binding.root
    }


    private fun setObserver() {

        viewModel.contactInfo.observe(viewLifecycleOwner) {
            it.apply {
                binding.name?.text = name
                binding.address?.text = address?.city ?: "N/A"
                binding.phone?.text  = phone
            }
        }

        viewModel.contactsList.observe(viewLifecycleOwner) {
            setAdapter(it)
        }
    }

    private fun setMenuBar() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.search -> {
                        handleSearch()
                        true
                    }

                    R.id.sortAscending -> {
                        viewModel.sortContactsInAscending()
                        true
                    }

                    R.id.sortDescending -> {
                        viewModel.sortContactsInDescending()
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.STARTED)
    }

    private fun handleSearch() {

        if (binding.etScanInput.isVisible) {
            binding.etScanInput.visibility = View.GONE
        } else {
            binding.etScanInput.visibility = View.VISIBLE
        }

        binding.etScanText.doOnTextChanged { query, _, _, _ ->
            if (query?.isNotEmpty() == true) {
                val list = viewModel.searchContacts(query.toString())
                adapter.initList(list)
            } else{
                adapter.initList(responseData)
            }
        }
    }

    private fun getContactsData() {
        viewModel.getContacts().observe(viewLifecycleOwner) {
            when(it) {

                is NetworkStates.Loading -> {
                    binding.progressBar?.visibility = View.VISIBLE
                }

                is NetworkStates.Success -> {
                    binding.progressBar?.visibility = View.GONE
                    val response = it.data
                    responseData = response
                    setAdapter(response)
                    viewModel.setContacts(response)
                }

                is NetworkStates.Error -> {
                    binding.progressBar?.visibility = View.GONE
                    Toast.makeText(requireContext(),"An Error Occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setAdapter(data: ArrayList<UsersModel>) {
        adapter = ContactListAdapter(data) { userData ->
            setContactsData(userData)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setContactsData(userData: UsersModel) {
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            handleLandscapeData(userData)
        } else {
            viewModel.setContactInfo(userData)
            findNavController().navigate(R.id.action_contactListFragment_to_contactInfoFragment)
        }
    }

    private fun handleLandscapeData(userData: UsersModel) {
            userData.apply {
                binding.name?.text = name
                binding.address?.text = address?.city ?: "N/A"
                binding.phone?.text  = phone
            }

           binding.iconDeleteContact?.setOnClickListener {
               userData.id?.let { id ->
                   viewModel.deleteContact(id)
               }
           }
    }
}