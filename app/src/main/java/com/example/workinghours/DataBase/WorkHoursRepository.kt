package com.example.workinghours.DataBase

import androidx.lifecycle.LiveData


class WorkHoursRepository(private val wordDao: WorkHoursDao) {


    val allData: LiveData<List<WorkHours>> = wordDao.getAllData()

    suspend fun insert(word: WorkHours) {
        wordDao.insertData(word)
    }

    suspend fun deleteData(day: Int, month: Int, year: Int) {
        wordDao.deleteSpecific(day , month , year)
    }


    suspend fun deleteAllData() {
        wordDao.deleteAll()
    }
}