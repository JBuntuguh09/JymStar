package com.lonewolf.jymstar.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Exercises")
data class Exercises (
    @PrimaryKey (autoGenerate = true)
    val exerciseId : Int,
    val name: String,
    val url : String,
    val equipment :String,
    val target : String,
    val bodyPart : String,
    val mainId : String

    )