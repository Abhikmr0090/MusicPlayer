package com.example.payruptask.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.payruptask.network.NetworkStates
import com.example.payruptask.network.UsersModel
import kotlinx.coroutines.launch

class ContactsViewModel(private val repo: HomeRepository) : ViewModel() {

    private val _contactsList = MutableLiveData<ArrayList<UsersModel>>()
    val contactsList: LiveData<ArrayList<UsersModel>> = _contactsList

    private val _contactInfo = MutableLiveData<UsersModel>()
    val contactInfo: LiveData<UsersModel> = _contactInfo

    fun setContacts(usersList: ArrayList<UsersModel>) {
        _contactsList.value = usersList
    }

    fun setContactInfo(usersModel: UsersModel) {
        _contactInfo.value = usersModel
    }

    fun searchContacts(query: String): ArrayList<UsersModel> {
        val allContacts = _contactsList.value ?: arrayListOf()

        return if (query.isEmpty()) {
            _contactsList.value ?: arrayListOf()
        } else {
            allContacts.filter { contact ->
                contact.name?.contains(query, ignoreCase = true) == true
            } as ArrayList<UsersModel>
        }
    }

    fun sortContactsInAscending() {
        _contactsList.value?.sortBy {
            it.name
        }
        _contactsList.value = _contactsList.value
    }

    fun sortContactsInDescending() {
        _contactsList.value?.sortByDescending {
            it.name
        }
        _contactsList.value = _contactsList.value
    }

    fun deleteContact(id: Int) {
        val value = _contactsList.value?.find {
            it.id == id
        }
        _contactsList.value?.remove(value)
        _contactsList.value = _contactsList.value

        if (_contactsList.value?.isNotEmpty() == true) {
            _contactInfo.value = _contactsList.value?.get(0)
        }
    }

    fun getContacts(): MutableLiveData<NetworkStates<ArrayList<UsersModel>>> {
        val mutableLiveData = MutableLiveData<NetworkStates<ArrayList<UsersModel>>>()
        mutableLiveData.value = NetworkStates.Loading
        viewModelScope.launch {
            mutableLiveData.value = repo.getContacts()
        }
        return mutableLiveData
    }

}