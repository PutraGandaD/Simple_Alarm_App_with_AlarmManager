package com.putragandad.alarmmanagerexercise

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.putragandad.alarmmanagerexercise.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var timePicker: MaterialTimePicker
    private lateinit var calendar : Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        createNotificationChannel()

        binding.btnSetAlarm.setOnClickListener {
            showTimePicker()
        }

        binding.btnSelectTime.setOnClickListener {
            setAlarm()
        }

        binding.btnCancelAlarm.setOnClickListener {
            cancelAlarm()
        }
    }

    private fun cancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_MUTABLE)

        alarmManager.cancel(pendingIntent)

        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_LONG).show()
    }

    private fun setAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        alarmManager.setRepeating(
            RTC_WAKEUP.
        )


        Toast.makeText(this, "Alarm set successfullly", Toast.LENGTH_LONG).show()
    }

    private fun showTimePicker() {
        timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()

        timePicker.show(supportFragmentManager, "alarmManagerExercise")

        timePicker.addOnPositiveButtonClickListener {
            if (timePicker.hour > 12) {
                binding.tvCurrentAlarmSet.text =
                    String.format("%02d", timePicker.hour - 12) + " : " +
                            String.format("%02d", timePicker.minute) + " PM"
            } else {
                binding.tvCurrentAlarmSet.text =
                    String.format("%02d", timePicker.hour) + " : " +
                            String.format("%02d", timePicker.minute) + " AM"
            }
        }

        calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = timePicker.hour
        calendar[Calendar.MINUTE] = timePicker.minute
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
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