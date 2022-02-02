package com.example.ispend

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_calendar_spend.*
import java.text.DecimalFormat

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


        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var date: String = "0"
            var valueDB: Double = 0.0

//            var listValueAndDate = HashMap<String,Double>()



            if(month < 9){
                if(dayOfMonth < 9){
                    date = "0${dayOfMonth}/0${month+1}/${year}"
                }else{
                    date = "${dayOfMonth}/0${month+1}/${year}"
                }
            }else{
                date = "${dayOfMonth}/${month+1}/${year}"
            }

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

//                listValueAndDate.put(formatDBDate, dbValue)
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

//                    listValueAndDate.put(formatDBDate, dbValue)
                }
            }

//            Log.d("Final_Value", "Final value is = ${listValueAndDate.values}")
            Log.d(TAG, "Date is = ${date}")

            Log.d("FINAL_VALUE", "Value is = ${valueDB}")

            val finalValue = formatValue(valueDB)

            tvAmount.text = finalValue

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