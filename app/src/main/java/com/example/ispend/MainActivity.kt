package com.example.ispend

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.spend_holder.*
import kotlinx.android.synthetic.main.spend_holder.view.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity(), fragment_addSpend.OnInputListener{

    val TAG = "MAIN_ACTIVITY"
    val db = DBHelper(this, null)

    val spendList = ArrayList<Spend>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Floating action button
        calendarSpendbtn.setImageResource(R.drawable.ic_calendar)
        calendarSpendbtn.setOnClickListener{
            val intent = Intent(this, CalendarSpend::class.java)
            startActivity(intent)
        }

        insertingToSpendAdapter(rvSpend, this, spendList)

        updateExpenditureTotal(spendList)


        btn_addSpend.setOnClickListener(){
            val add_spend = fragment_addSpend()
            add_spend.show(supportFragmentManager, "add_spend")
        }

    }

    @SuppressLint("Range")
    override fun sendInput(value: Float, type: String, date: String) {
        Log.d(TAG, "sendInput = Spend Value: $value, Spend Type: $type, Spend Date: $date")
        db.insertValueDB(value, type, date)

        // to refresh the recyclerview
        var dateToTime = splitTheDate(date)

        val cursor = db.getValueDB()
        cursor?.moveToLast()
        var id = cursor?.getInt(cursor.getColumnIndex(DBHelper.ID_COL))
        id?.let {
            Spend(it,formattedDecimalPlaces(value.toDouble()).toDouble(), type, dateToTime) }?.let {
                spendList.add(it)
            }
        updateExpenditureTotal(spendList)

    }




    private fun insertingToSpendAdapter(rv: RecyclerView, context: Context, spendList: ArrayList<Spend>){

//        val spendList = ArrayList<Spend>()

        // get the value from DB
        retrieveValueDB(spendList)

        val adapter = SpendAdapter(spendList)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)
        adapter.notifyItemInserted(spendList.size -1)

        // delete item using swipe gesture
        deleteItemRV(1, adapter)
    }



    @SuppressLint("Range")
    fun retrieveValueDB(spendList: ArrayList<Spend>){

        val cursor = db.getValueDB()

        if(cursor!!.moveToFirst()){
            var dbID = cursor.getInt(cursor.getColumnIndex(DBHelper.ID_COL))
            var dbValue = cursor.getDouble(cursor.getColumnIndex(DBHelper.SPEND_COL))
            var dbType = cursor.getString(cursor.getColumnIndex(DBHelper.SPEND_TYPE))
            var dbDate = cursor.getString(cursor.getColumnIndex(DBHelper.SPEND_DATE))

            var time = splitTheDate(dbDate)

            Log.d("DB", "retrieveValueDB: $time")

            spendList.add(Spend(dbID, formattedDecimalPlaces(dbValue).toDouble(), dbType, time))

            while (cursor.moveToNext()){
                var dbID = cursor.getInt(cursor.getColumnIndex(DBHelper.ID_COL))
                var dbValue = cursor.getDouble(cursor.getColumnIndex(DBHelper.SPEND_COL))
                var dbType = cursor.getString(cursor.getColumnIndex(DBHelper.SPEND_TYPE))
                var dbDate = cursor.getString(cursor.getColumnIndex(DBHelper.SPEND_DATE))

                var time = splitTheDate(dbDate)

                spendList.add(Spend(dbID, formattedDecimalPlaces(dbValue).toDouble(), dbType, time))
                Log.d("MOVETONEXT", "retrieveValueDB: ${formattedDecimalPlaces(dbValue).toDouble()}")
                Log.d("DATABASE_VALUE", "ID: $dbID, Value: $dbValue, Type: $dbType, Date: $dbDate")
            }
        }

        cursor.close()
    }



    fun updateExpenditureTotal(spendList: ArrayList<Spend>){
        val decimal = DecimalFormat("#,###.##")

        val totalSpendValue: Double = spendList.map { it.spend_value }.sum()
//        tvSpendValue.text = totalSpendValue.toString()
        tvSpendValue.text = formattedDecimalPlaces(totalSpendValue)

    }

    // convert to 2 decimal places
    fun formattedDecimalPlaces(number: Double): String{
        val decimal = DecimalFormat("####.00")

        return decimal.format(number)

    }

    fun splitTheDate(date: String): String{
        return date.split(" ")[0]
    }



    fun deleteItemRV(position: Int, adapter: SpendAdapter){

        val swipeGesture = object : SwipteGesture(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                super.onSwiped(viewHolder, direction)
                when(direction){
                    ItemTouchHelper.RIGHT -> {
//                        Toast.makeText(this@MainActivity, "Im number ${viewHolder.position}", Toast.LENGTH_SHORT).show()

                        // Delete based on date
                        val idInDatabase = viewHolder.itemView.spend_id.text
                        Log.d("ONSWIPED", "onSwiped: $idInDatabase")

                        db.deleteData(idInDatabase as String)
                        spendList.removeAt(viewHolder.position)

                        adapter.notifyDataSetChanged()

                        // update the expenditure total
                        updateExpenditureTotal(spendList)

                    }
                }

            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(rvSpend)





    }










}




