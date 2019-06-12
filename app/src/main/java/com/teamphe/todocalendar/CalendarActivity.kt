package com.teamphe.todocalendar

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events.*
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import kotlin.Int
import android.widget.Toast
import com.teamphe.todocalendar.R
import java.util.*

class CalendarActivity : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myView = inflater!!.inflate(R.layout.fragment_calendar, container, false)

        val calendarView = myView.findViewById<CalendarView>(R.id.calendar)
        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
            Toast.makeText(this@CalendarActivity.context, msg, Toast.LENGTH_SHORT).show()
        }
        return myView
    }
}