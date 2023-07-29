package com.lonewolf.jymstar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkoutDao {

    @Insert
    fun insert(workout: Workout)

    @Update
    fun update(workout: Workout)

    @Delete
    fun delete(workout: Workout)

    @Query("Delete from Workout where userId == :userId")
    fun deleteAll(userId:String)

    @Query("Delete from Workout where workoutId == :workoutId")
    fun deleteWorkout(workoutId:Int)

    @Query("select * from Workout where userId == :userId")
    fun selectAll(userId: String) : LiveData<List<Workout>>

    @Query("Select * from Workout where workoutId == :workoutId")
    fun selectWorkout(workoutId: Int) : LiveData<List<Workout>>
}