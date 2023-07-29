package com.lonewolf.jymstar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
@Dao
interface ExerciseDao {

    @Insert
    fun insert(exercises: Exercises)

    @Update
    fun update(exercises: Exercises)

    @Delete
    fun delete(exercises: Exercises)

    @Query("Select * from Exercises")
    fun selectAll() : LiveData<List<Exercises>>

    @Query("Select * from Exercises where exerciseId == :exId")
    fun selectExercise(exId:Int) : LiveData<List<Exercises>>
}