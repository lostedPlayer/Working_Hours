package com.example.workinghours.DataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WORK_HOURS")
data class WorkHours(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ID")  val id: Int,
    @ColumnInfo(name = "DAY") val day: Int,
    @ColumnInfo(name = "MONTH") val month: Int,
    @ColumnInfo(name = "YEAR") val year: Int,
    @ColumnInfo(name = "WORKED HOURS") val hoursWorked: Double

)