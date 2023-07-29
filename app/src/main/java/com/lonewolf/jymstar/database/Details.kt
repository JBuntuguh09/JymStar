package com.lonewolf.jymstar.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Details")
data class Details (
    @PrimaryKey (autoGenerate = true)
    val detailId : Int,
    val exerciseName : String,
    val category : String,
    val details : String,
    val difficulty : String,
    val force : String,
    val grips : String,
    val steps : String,
    val target : String,
    val videoUrl : String,
    val youtubeURL : String,
    val id : String
        )

