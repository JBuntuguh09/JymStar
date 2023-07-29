package com.lonewolf.jymstar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Exercises::class, Details::class, Workout::class], version = 4)
abstract class MainDb : RoomDatabase() {

    abstract fun exerciseDao() : ExerciseDao
    abstract fun detailsDao() : DetailsDao
    abstract fun workoutDao() : WorkoutDao

    companion object {
        @Volatile
        var instance : MainDb? = null

        fun getInstance(context: Context): MainDb {
            val tempInstance = instance
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this) {
                val nInstance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDb::class.java,
                    "Main_Db"
                ).fallbackToDestructiveMigration()
                    .build()
                instance = nInstance
                return nInstance
            }


        }
    }


}