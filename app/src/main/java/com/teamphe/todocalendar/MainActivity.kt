package com.teamphe.todocalendar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val todoBtn = findViewById<Button>(R.id.btn_todolist)
        val calendarBtn = findViewById<Button>(R.id.btn_calendar)

        ShowFragmentOne()

        todoBtn.setOnClickListener {
            ShowFragmentOne()
        }

        calendarBtn.setOnClickListener {
            ShowFragmentTwo()
        }
    }

    fun ShowFragmentOne() {
        val transaction = manager.beginTransaction()
        val fragment = DashboardActivity()
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun ShowFragmentTwo() {
        val transaction = manager.beginTransaction()
        val fragment = CalendarActivity()
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
