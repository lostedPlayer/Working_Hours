package com.example.workinghours.DataBase

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public class WorkHoursViewModel(application: Application) : AndroidViewModel(application) {


    var mRepository: WorkHoursRepository
    var list: LiveData<List<WorkHours>>

    //get database and all data from db
    init {
        val db = WorkHoursDatabase.getDatabase(application)
        val dao = db.workHoursDao()
        mRepository = WorkHoursRepository(dao)
        list = mRepository.allData
    }

    fun getAllData(): LiveData<List<WorkHours>> {
        return list
    }
    fun deleteData(day: Int, month: Int, year: Int) = viewModelScope.launch(Dispatchers.IO) {
        mRepository.deleteData(day, month, year)
    }
    fun insertData(data: WorkHours) = viewModelScope.launch(Dispatchers.IO) {
        mRepository.insert(data)
    }

    fun deleteAllData() = viewModelScope.launch(Dispatchers.IO) {
        mRepository.deleteAllData()
    }
}




