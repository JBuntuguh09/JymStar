package com.lonewolf.jymstar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.glxn.qrgen.android.QRCode

@Entity(tableName = "Workout")
class Workout(
    @PrimaryKey(autoGenerate = true)
    val workoutId : Int,
    val workoutName : String,
    val exerciseName : String,
    val code :String,
    val qrCode: String,
    val url : String,
    val reps : String,
    val count : String,
    val time : String,
    val target : String,
    val equipment : String,
    val bodyPart :String,
    val mainId :String,
    val userId : String

)