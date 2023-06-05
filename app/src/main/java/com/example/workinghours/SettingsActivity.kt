package com.example.workinghours

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    lateinit var setReminderButton: LinearLayout

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent


    private lateinit var  sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //get shared preference from system
        sharedPref = this.getPreferences(Context.MODE_PRIVATE)


        // Get Views
        setReminderButton = findViewById(R.id.setReminder_bt)



        // Create the notification channel
        val channelId = "Working_Hours_APP"
        val channelName = "Working_Hours_APP"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        // Initialize AlarmManager
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Create PendingIntent for the notification
        val notificationIntent = Intent(this, NotificationReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        // Show dialog to pick reminder Time
        setReminderButton.setOnClickListener {
            val time = Calendar.getInstance()
            val hour = time.get(Calendar.HOUR_OF_DAY)
            val minute = time.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    // After the user has chosen the time, schedule the notification
                    scheduleNotification(hourOfDay, minute)

                }, hour, minute, true
            ).show()
        }
    }

    private fun scheduleNotification(hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // Set the notification to repeat every day
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        // Log the scheduled time for debugging purposes
        Log.d(
            "SettingActivity",
            "Notification scheduled for: ${calendar.time}"
        )
    }
}
