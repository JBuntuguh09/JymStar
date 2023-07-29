package com.lonewolf.jymstar.database

import androidx.lifecycle.LiveData

class ExerciseRepo(private val exerciseDao: ExerciseDao, exId : Int) {

    val liveData : LiveData<List<Exercises>> = exerciseDao.selectAll()
    val liveExercise : LiveData<List<Exercises>> = exerciseDao.selectExercise(exId)

    suspend fun insert(exercises: Exercises){
        exerciseDao.insert(exercises)
    }

}