package com.example.payruptask.ui

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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.payruptask.R
import com.example.payruptask.databinding.FragmentContactInfoBinding
import com.example.payruptask.network.ApiHandler

class ContactInfoFragment : Fragment() {

    private lateinit var binding : FragmentContactInfoBinding
    private val viewModel        : ContactsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_contact_info, container, false)
        setObserver()
        setMenuBar()
        return binding.root
    }

    private fun setObserver() {
        viewModel.contactInfo.observe(viewLifecycleOwner) {
            it.apply {
                binding.name.text    = name
                binding.address.text = address?.street
                binding.phone.text   = phone
            }
        }
    }

    private fun setMenuBar() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.contact_info_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.deleteContact -> {
                        deleteContact()
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.STARTED)
    }

    private fun deleteContact() {
        viewModel.contactInfo.value?.id?.let { viewModel.deleteContact(it) }
        Toast.makeText(requireContext(),"Contact Deleted",Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }
}