package com.lonewolf.jymstar.database

import androidx.lifecycle.LiveData

class WorkoutRepo(private val workoutDao: WorkoutDao) {

    suspend fun insert(workout: Workout){
        workoutDao.insert(workout)
    }

    suspend fun getAll(userId:String):LiveData<List<Workout>>{
        return workoutDao.selectAll(userId)
    }

    suspend fun getWorkout(workoutId:Int):  LiveData<List<Workout>>{
        return workoutDao.selectWorkout(workoutId)
    }

    suspend fun deleteAll(userId: String){
        workoutDao.deleteAll(userId)
    }

    suspend fun deleteWorkout(workoutId: Int){
        workoutDao.deleteWorkout(workoutId)
    }

}