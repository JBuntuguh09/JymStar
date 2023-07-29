package com.lonewolf.jymstar.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lonewolf.jymstar.resources.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: WorkoutRepo

    init {
        val workout = MainDb.getInstance(application).workoutDao()
        val storage = Storage(application)
        repo = WorkoutRepo(workout)

    }

    fun insert(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(workout)
        }
    }

    fun getAll(userId:String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAll(userId)
        }
    }

    fun getWorkout(workoutId : Int){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getWorkout(workoutId)
        }
    }

    fun deleteAdll(userId:String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAll(userId)
        }
    }

    fun getAll(workoutId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteWorkout(workoutId)
        }
    }

}