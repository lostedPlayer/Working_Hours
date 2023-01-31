package com.example.workinghours

import android.app.Application
import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.workinghours.DataBase.WorkHours
import com.example.workinghours.DataBase.WorkHoursViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.MonthScrollListener
import nl.joery.timerangepicker.TimeRangePicker
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    //Views
    lateinit var calendarView: CalendarView
    lateinit var tv_TogetherHoursThisMonth: TextView
    lateinit var tv_paycheck: TextView


    lateinit var mViewModel: WorkHoursViewModel
    lateinit var list: List<WorkHours>
    var togetherHours: Double = 0.0
    var mSelectedMonth: Int = 1
    var mSelectedYear: Int = 1


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        calendarView = findViewById(R.id.cv_main)
        tv_TogetherHoursThisMonth = findViewById(R.id.tv_togetherHoursThisMonth)
        tv_paycheck = findViewById(R.id.tv_paycheck)
        list = ArrayList<WorkHours>()


        //get view model for Room Database
        mViewModel = ViewModelProvider(this).get(WorkHoursViewModel::class.java)

        // TODO: ONLY FOR TEST REMOVE LATER

        calendarDaySetup()
        calendarMonthSetup()


        //Getting data from database
        mViewModel.getAllData().observe(this, androidx.lifecycle.Observer { list ->
            this.list = list
            Log.d("M1", "DataBaza podatci " + list.toString())
            togetherHours = 0.0
            for (data in list) {
                if (data.month == mSelectedMonth) {
                    togetherHours += data.hoursWorked
                    tv_TogetherHoursThisMonth.text = togetherHours.toString()
                }
            }
            calendarDaySetup()
            calendarView.notifyCalendarChanged()
        })

        //month scroll listener
        calendarView.monthScrollListener = object : MonthScrollListener {
            override fun invoke(scrolledToMonth: CalendarMonth) {

                togetherHours = 0.0
                tv_TogetherHoursThisMonth.text = togetherHours.toString()
                for (data in list) {
                    Log.d(
                        "M1",
                        "Database: ${data.month} , ${data.year}  calendar data ${scrolledToMonth.month}/${scrolledToMonth.year} "
                    )
                    if (data.month == scrolledToMonth.month && data.year == scrolledToMonth.year) {
                        togetherHours += data.hoursWorked
                        tv_TogetherHoursThisMonth.text = togetherHours.toString()
                    }
                }
            }

        }


    }

    //day setup for calendar View
    fun calendarDaySetup() {

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun bind(container: DayViewContainer, sDay: CalendarDay) {


                container.tv_date.text = sDay.date.dayOfMonth.toString()

                //adding new data to database
                container.cell_box.setOnClickListener {
                    if (container.tv_workHours.visibility.equals(View.INVISIBLE)) {
                        val date = sDay.date
                        addDataDialog(date.dayOfMonth, date.monthValue, date.year)
                    }
                }

                //deleting data here on long calendar cell click
                container.cell_box.setOnLongClickListener {
                    if (container.tv_workHours.visibility.equals(View.VISIBLE)) {
                        val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                            .setTitle("Do you want to delete Work Hours for this day?")
                            .setPositiveButton(
                                "Delete",
                                DialogInterface.OnClickListener { dialog, which ->

                                    val date = sDay.date
                                    mViewModel.deleteData(
                                        date.dayOfMonth,
                                        date.monthValue,
                                        date.year
                                    )


                                })
                            .setNegativeButton("Cancle", null)
                            .show()

                    }
                    false
                }



                container.tv_workHours.visibility = View.INVISIBLE
                for (data in list) {
                    val CalendarDay = sDay.date.dayOfMonth
                    val CalendarMonth = sDay.date.month.value
                    val CalendarYear = sDay.date.year

                    if (CalendarDay.equals(data.day) && CalendarMonth.equals(data.month) && CalendarYear.equals(
                            data.year
                        )
                    ) {
                        container.tv_workHours.visibility = View.VISIBLE
                        container.tv_workHours.text = data.hoursWorked.toString()
                    }
                }
            }

            override fun create(view: View): DayViewContainer {
                return DayViewContainer(view)
            }

        }


    }

    fun calendarMonthSetup() {
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                container.tv_month.text = "${month.yearMonth.month.name} / ${month.year}"

            }


            override fun create(view: View): MonthViewContainer {
                return MonthViewContainer(view)
            }


        }

        val currentMonth = YearMonth.now()

        val firstMonth = currentMonth.minusMonths(30)
        val lastMonth = currentMonth.plusMonths(50)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)


    }

    //show add data dialog
    fun addDataDialog(day: Int, month: Int, year: Int) {

        var mWorkedHours: Double = 8.0

        val mDialogBuilder = AlertDialog.Builder(this).create()
        val view = LayoutInflater.from(this).inflate(R.layout.add_data_dialog_layout, null)
        val mTimePicker = view.findViewById<TimeRangePicker>(R.id.add_data_dialog_timePicker)
        val mHourWorkedTextView = view.findViewById<TextView>(R.id.add_data_dialog_tv_hoursWorked)
        val mSaveButton = view.findViewById<Button>(R.id.add_data_dialog_button_save)
        val mCancleButton = view.findViewById<Button>(R.id.add_data_dialog_button_cancle)
        val mSelectedDate_tv =
            view.findViewById<TextView>(R.id.add_data_dialog_SelectedDate_textView)

        mSelectedDate_tv.text = "$day:$month:$year"

        //time picker
        mTimePicker.setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener {
            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
                val workedHours_display_value = "Worked: ${duration.hour}:${duration.minute} H"
                mHourWorkedTextView.text = workedHours_display_value

                var wh: String = "${duration.hour}.${duration.minute}"
                mWorkedHours = wh.toDouble()
            }

            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {

            }

            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {

            }
        })

        //save and cancle buttons
        mCancleButton.setOnClickListener {
            mDialogBuilder.dismiss()
        }
        mSaveButton.setOnClickListener {
            val newData = WorkHours(0, day, month, year, mWorkedHours)
            mViewModel.insertData(newData)
            mDialogBuilder.dismiss()

        }

        mDialogBuilder.setView(view)
        mDialogBuilder.show()
    }


}


