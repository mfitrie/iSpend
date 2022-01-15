package com.example.ispend

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity(), fragment_addSpend.OnInputListener{

    val TAG = "MAIN_ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        insertingToSpendAdapter(rvSpend, this)


        btn_addSpend.setOnClickListener(){
            val add_spend = fragment_addSpend()
            add_spend.show(supportFragmentManager, "add_spend")
        }

    }

    @SuppressLint("Range")
    override fun sendInput(value: Float, type: String, date: String) {
        Log.d(TAG, "sendInput = Spend Value: $value, Spend Type: $type, Spend Date: $date")

        // insert to database
        val db = DBHelper(this, null)
        db.insertValueDB(value, type, date)

        val cursor = db.getValueDB()
        cursor!!.moveToFirst()


        while(cursor.moveToNext()){
            var dbValue = cursor.getDouble(cursor.getColumnIndex(DBHelper.SPEND_COL))
            var dbType = cursor.getString(cursor.getColumnIndex(DBHelper.SPEND_TYPE))
            var dbDate = cursor.getString(cursor.getColumnIndex(DBHelper.SPEND_DATE))
            Log.d("VALUE_FROM_DATABASE", "sendInput = Spend Value: $dbValue, Spend Type: $dbType, Spend Date: $dbDate")

        }


        cursor.close()


    }


}




fun insertingToSpendAdapter(rv: RecyclerView, context: Context){
    var spendList = mutableListOf(
        Spend(20.00, "Food", "20/2/2021"),
        Spend(20.00, "Food", "20/2/2021"),
        Spend(20.00, "Food", "20/2/2021"),
        Spend(20.00, "Food", "20/2/2021"),
        Spend(20.00, "Food", "20/2/2021"),
        Spend(20.00, "Food", "20/2/2021"),
        Spend(20.00, "Food", "20/2/2021"),
        Spend(20.00, "Food", "20/2/2021"),
        Spend(20.00, "Food", "20/2/2021"),
        Spend(20.00, "Food", "20/2/2021"),
    )

    val adapter = SpendAdapter(spendList)

    rv.adapter = adapter
    rv.layoutManager = LinearLayoutManager(context)
//    rvSpend.adapter = adapter
//    rvSpend.layoutManager = LinearLayoutManager(this)


    adapter.notifyItemInserted(spendList.size -1)
}