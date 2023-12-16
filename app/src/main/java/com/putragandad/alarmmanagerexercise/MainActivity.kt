package com.putragandad.alarmmanagerexercise

import android.app.AlarmManager
import android.app.AlarmManager.INTERVAL_DAY
import android.app.AlarmManager.RTC_WAKEUP
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.putragandad.alarmmanagerexercise.databinding.ActivityMainBinding
import java.util.Calendar
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var alarmManager: AlarmManager

    private lateinit var alarmManagerIntent1 : PendingIntent
    private lateinit var alarmManagerIntent2 : PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        binding.btnSetAlarm1.setOnClickListener {
            setAlarm1("This is Alarm 1")
        }

        binding.btnSetAlarm2.setOnClickListener {
            setAlarm2("This is Alarm 2")
        }


    }

    private fun cancelAlarm1() {
        alarmManagerIntent1.cancel()
        Toast.makeText(this, "Alarm 1 now has been disabled", Toast.LENGTH_LONG).show()
    }

    private fun cancelAlarm2() {
        alarmManagerIntent2.cancel()
        Toast.makeText(this, "Alarm 2 now has been disabled", Toast.LENGTH_LONG).show()
    }

    private fun setAlarm1(alarmNotificationText: String) {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        alarmManagerIntent1 = getAlarmIntent(alarmNotificationText)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 5)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmManagerIntent1
        )

        Toast.makeText(this, "Alarm set successfullly", Toast.LENGTH_LONG).show()
    }

    private fun setAlarm2(alarmNotificationText: String) {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        alarmManagerIntent2 = getAlarmIntent(alarmNotificationText)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 10)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmManagerIntent2
        )

        Toast.makeText(this, "Alarm set successfullly", Toast.LENGTH_LONG).show()
    }

    private fun getAlarmIntent(notificationContent: String) : PendingIntent {
        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("customText", notificationContent)
        }
        return PendingIntent.getBroadcast(this, UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "alarmManagerReminderChannel"
            val description = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("alarmManagerExercise", name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(channel)

        }
    }
}