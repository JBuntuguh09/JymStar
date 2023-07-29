package com.lonewolf.jymstar.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lonewolf.jymstar.resources.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    val liveData: LiveData<List<Details>>
    private val repo: DetailsRepo

    init {
        val exercise = MainDb.getInstance(application).detailsDao()
        val storage = Storage(application)
        repo = DetailsRepo(exercise, storage.selectedId!!)
        liveData = repo.liveData
    }

    fun insert(details: Details) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(details)
        }
    }

     fun getEquipment(type:String) : LiveData<List<Details>> {
//        viewModelScope.launch(Dispatchers.IO) {
//            repo.getEquipment(type)
//        }
        return repo.getEquipment(type)
    }

}