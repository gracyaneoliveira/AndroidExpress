package com.example.beberaguar

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnNotify: Button
    private lateinit var editMinutes: EditText
    private lateinit var timePicker: TimePicker

    private var hour: Int = 0
    private var minute: Int = 0
    private var interval: Int = 0

    private var activated = false

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setComponents()
        loadDataFromPreferences()
    }

    private fun setComponents() {
        btnNotify = findViewById(R.id.btn_notify)
        editMinutes = findViewById(R.id.edit_txt_number_interval)
        timePicker = findViewById(R.id.time_picker)
        timePicker.setIs24HourView(true)

        preferences = getSharedPreferences("waterdb", Context.MODE_PRIVATE)

        btnNotify.setOnClickListener { notifyClick() }
    }

    private fun loadDataFromPreferences() {
        activated = preferences.getBoolean(ACTIVATED, false)
        if (activated) {
            updateBtnNotify(R.string.pause, android.R.color.black)
            activated = true

            val interval = preferences.getInt(INTERVAL, 0)
            val hour = preferences.getInt(HOUR, timePicker.currentHour)
            val minute = preferences.getInt(MINTUTE, timePicker.currentMinute)

            editMinutes.text = Editable.Factory.getInstance().newEditable(interval.toString())
            timePicker.currentHour = hour
            timePicker.currentMinute = minute
        }
    }

    private fun notifyClick() {
        val sInterval = editMinutes.text.toString()

        if (sInterval.isEmpty()) {
            Toast.makeText(this, R.string.error_msg, Toast.LENGTH_LONG).show()
            return
        }

        hour = timePicker.currentHour
        minute = timePicker.currentMinute
        interval = Integer.parseInt(sInterval)

        if (!activated) {
            updateBtnNotify(R.string.pause, android.R.color.black)
            activated = true

            val editor = preferences.edit()
            editor.putBoolean(ACTIVATED, true)
            editor.putInt(INTERVAL, interval)
            editor.putInt(MINTUTE, minute)
            editor.putInt(HOUR, hour)
            editor.apply()

        } else {
            updateBtnNotify(R.string.notify, R.color.colorAccent)
            activated = false

            val editor = preferences.edit()
            editor.putBoolean(ACTIVATED, false)
            editor.remove(INTERVAL)
            editor.remove(MINTUTE)
            editor.remove(HOUR)
            editor.apply()
        }
        Log.d("Teste", "hora : $hour minuto: $minute intervalo: $interval")
    }

    private fun updateBtnNotify(textRid: Int, colorRid: Int) {
        btnNotify.setText(textRid)
        btnNotify.setBackgroundColor(ContextCompat.getColor(this, colorRid))
    }

    companion object {
        const val ACTIVATED = "activated"
        const val INTERVAL = "interval"
        const val MINTUTE = "MINUTE"
        const val HOUR = "HOUR"
    }

}