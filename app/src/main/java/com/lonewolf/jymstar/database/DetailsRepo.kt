package com.lonewolf.jymstar.database

import androidx.lifecycle.LiveData

class DetailsRepo(private val detailsDao: DetailsDao, exId : String) {

    val liveData : LiveData<List<Details>> = detailsDao.selectAll()
    val liveExercise : LiveData<List<Details>> = detailsDao.selectDetail(exId)

    suspend fun insert(details: Details){
        detailsDao.insert(details)
    }

     fun getEquipment(type:String):LiveData<List<Details>>{
        return detailsDao.selectEquipment(type)
    }

}