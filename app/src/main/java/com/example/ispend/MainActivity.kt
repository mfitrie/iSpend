package com.example.ispend

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.spend_holder.*
import kotlinx.android.synthetic.main.spend_holder.view.*

class MainActivity : AppCompatActivity(), fragment_addSpend.OnInputListener{

    val TAG = "MAIN_ACTIVITY"
    val db = DBHelper(this, null)

    val spendList = ArrayList<Spend>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        val cursor = db.getValueDB()
        cursor?.moveToLast()
        var id = cursor?.getInt(cursor.getColumnIndex(DBHelper.ID_COL))
        id?.let {
            Spend(it,value.toDouble(), type, date) }?.let {
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

            spendList.add(Spend(dbID, dbValue, dbType, dbDate))

            while (cursor.moveToNext()){
                var dbID = cursor.getInt(cursor.getColumnIndex(DBHelper.ID_COL))
                var dbValue = cursor.getDouble(cursor.getColumnIndex(DBHelper.SPEND_COL))
                var dbType = cursor.getString(cursor.getColumnIndex(DBHelper.SPEND_TYPE))
                var dbDate = cursor.getString(cursor.getColumnIndex(DBHelper.SPEND_DATE))

                spendList.add(Spend(dbID, dbValue, dbType, dbDate))
                Log.d("DATABASE_VALUE", "ID: $dbID, Value: $dbValue, Type: $dbType, Date: $dbDate")
            }
        }

        cursor.close()
    }



    fun updateExpenditureTotal(spendList: ArrayList<Spend>){
        val totalSpendValue: Double = spendList.map { it.spend_value }.sum()
        tvSpendValue.text = totalSpendValue.toString()
    }



    fun deleteItemRV(position: Int, adapter: SpendAdapter){

        val swipeGesture = object : SwipteGesture(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                super.onSwiped(viewHolder, direction)
                when(direction){
                    ItemTouchHelper.RIGHT -> {
//                        Toast.makeText(this@MainActivity, "Im number ${viewHolder.position}", Toast.LENGTH_SHORT).show()

                        // Delete based on date
                        db.deleteData(viewHolder.itemView.spend_date.text.toString())
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




