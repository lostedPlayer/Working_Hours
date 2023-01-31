package com.example.workinghours.DataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkHoursDao {

    @Insert
    fun insertData(data: WorkHours)

    @Delete
    fun delete(data: WorkHours)

    @Query("DELETE FROM WORK_HOURS WHERE DAY = :day  AND MONTH = :month AND YEAR = :year")
    fun deleteSpecific(day : Int , month:Int , year:Int)

    @Query("SELECT * FROM WORK_HOURS")
    fun getAllData(): LiveData<List<WorkHours>>

    @Query("DELETE FROM WORK_HOURS")
    fun deleteAll()
}