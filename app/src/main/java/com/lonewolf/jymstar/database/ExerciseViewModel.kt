package com.lonewolf.jymstar.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    val liveData : LiveData<List<Exercises>>
    private val repo : ExerciseRepo

    init {
        val exercise = MainDb.getInstance(application).exerciseDao()
        val exid = 1
        repo = ExerciseRepo(exercise, exid)
        liveData = repo.liveData
    }

    fun insert(exercises: Exercises){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(exercises)
        }
    }
}