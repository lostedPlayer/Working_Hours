package com.example.workinghours.DataBase

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executor
import androidx.lifecycle.*
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@androidx.room.Database(entities = arrayOf(WorkHours::class), version = 1, exportSchema = false)
public abstract class WorkHoursDatabase : RoomDatabase() {
    abstract fun workHoursDao(): WorkHoursDao

    companion object {
        @Volatile
        private var INSTANCE: WorkHoursDatabase? = null
        private val mExicutor = Executors.newSingleThreadExecutor()


        fun getDatabase(context: Context): WorkHoursDatabase {

            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkHoursDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                return instance
            }
        }
    }


}