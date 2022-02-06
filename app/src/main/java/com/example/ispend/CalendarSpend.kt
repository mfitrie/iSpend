package com.example.ispend

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.android.synthetic.main.activity_calendar_spend.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.Duration.Companion.milliseconds

class CalendarSpend : AppCompatActivity() {
    val TAG = "Calendar_Spend"

    val db = DBHelper(this, null)



    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_spend)

        fl_TotalDayExpenditure.apply {
            setImageResource(R.drawable.ic_day)
            setOnClickListener{
                val intent = Intent(this@CalendarSpend, MainActivity::class.java)
                startActivity(intent)
            }
        }


        calendarView.setUseThreeLetterAbbreviation(true)


        calendarView.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            @SuppressLint("SimpleDateFormat")
            override fun onDayClick(dateClicked: Date?) {
                    val dateFormatter = SimpleDateFormat("dd/MM/yyyy")

                    val formattedDate = dateFormatter.format(dateClicked).split("/")
                    val day = formattedDate[0]
                    val month = formattedDate[1]
                    val year = formattedDate[2]


                    Log.d("Calendar_View", "day: ${day}")
                    Log.d("Calendar_View", "month: ${month}")
                    Log.d("Calendar_View", "year: ${year}")

                    var date: String = "0"
                    var valueDB: Double = 0.0

                    date = "${day}/${month}/${year}"

                    Log.d("VALUE_DAY", date)


                    val cursor = db.getValueDB()
                    if(cursor!!.moveToFirst()){
                        var dbValue = cursor.getDouble(cursor.getColumnIndex(DBHelper.SPEND_COL))
                        var dbDate = cursor.getString(cursor.getColumnIndex(DBHelper.SPEND_DATE))


                        var formatDBDate = dateSplit(dbDate)

                        if(formatDBDate == date){
                            valueDB += dbValue
                            Log.d("in_while_first", "${valueDB}")
                        }

                        Log.d("IN_IF", formatDBDate)


                        while (cursor.moveToNext()){
                            var dbValue = cursor.getDouble(cursor.getColumnIndex(DBHelper.SPEND_COL))
                            var dbDate = cursor.getString(cursor.getColumnIndex(DBHelper.SPEND_DATE))


                            var formatDBDate = dateSplit(dbDate)
                            if(formatDBDate == date){
                                valueDB += dbValue
                                Log.d("in_while_other", "${valueDB}")
                            }

                            Log.d("IN_WHILE", formatDBDate)

                            Log.d("OTHER_VALUE_WHILE", dbDate)

                    }
                }

                Log.d(TAG, "Date is = ${date}")

                Log.d("FINAL_VALUE", "Value is = ${valueDB}")

                val finalValue = formatValue(valueDB)

                tvAmount.text = finalValue
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
            }

        })


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusDateSpend()
        }


    }



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    fun focusDateSpend(){
        val listOfDate = ArrayList<String>()
        val listEvent = ArrayList<Event>()

        val cursor = db.getValueDB()
        if(cursor!!.moveToFirst()){
            var dbDate = cursor.getString(cursor.getColumnIndex(DBHelper.SPEND_DATE))


            var formatDBDate = dateSplit(dbDate)
            listOfDate.add(formatDBDate)

            while (cursor.moveToNext()){
                var dbDate = cursor.getString(cursor.getColumnIndex(DBHelper.SPEND_DATE))


                var formatDBDate = dateSplit(dbDate)
                listOfDate.add(formatDBDate)

            }
        }



        for(i in listOfDate){
//            var event = Event(Color.GREEN, )
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val date = formatter.parse(i)

            val dateInMillis: Long = date.time


            calendarView.addEvent(Event(Color.parseColor("#FD7014"), dateInMillis))

            Log.d("list_date", "focusDateSpend: ${dateInMillis}")
        }

    }



    fun dateSplit(date:String): String{
        return date.split(' ')[0]
    }


    fun formatValue(value:Double):String{
        var df:DecimalFormat? = null

        if(value == 0.0){
            df = DecimalFormat("0.00")
            return df.format(value)
        }else{
            df = DecimalFormat("####.00")
            return df.format(value)
        }

    }






























}
