package com.example.workinghours

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    lateinit var setReminderButton: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setReminderButton = findViewById(R.id.setReminder_bt)


        //show dialog to pick reminder Time
        setReminderButton.setOnClickListener {

            val time = Calendar.getInstance()
            val hour = time.get(Calendar.HOUR_OF_DAY)
            val minute = time.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                    Log.d("SettingActivity", "Time Picker selected Time: ${hour.toString()} ${minute.toString()}")
                }, hour, minute, true
            ).show()


        }
    }
}