package com.lonewolf.jymstar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DetailsDao {

    @Insert
    fun insert(details: Details)

    @Update
    fun update(details: Details)

    @Delete
    fun delete(details: Details)

    @Query("select * from Details")
    fun selectAll() : LiveData<List<Details>>

    @Query("Select * from Details where id == :id")
    fun selectDetail(id:String) : LiveData<List<Details>>

    @Query("Select * from Details where category == :type")
    fun selectEquipment(type:String) : LiveData<List<Details>>
}