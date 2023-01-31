package com.example.workinghours

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kizitonwose.calendarview.ui.ViewContainer
import nl.joery.timerangepicker.TimeRangePicker

class DayViewContainer(view: View) : ViewContainer(view) {

    var tv_date = view.findViewById<TextView>(R.id.tv_date)
    var tv_workHours = view.findViewById<TextView>(R.id.tv_work_Hours)
    var cell_box = view.findViewById<RelativeLayout>(R.id.day_cell_box)


}


class MonthViewContainer(view: View) : ViewContainer(view) {
    val tv_month = view.findViewById<TextView>(R.id.tv_calendar_header)

    init {
        tv_month.getText()

    }

}

