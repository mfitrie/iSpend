package com.example.ispend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        rvSpend.adapter = adapter
        rvSpend.layoutManager = LinearLayoutManager(this)


        adapter.notifyItemInserted(spendList.size -1)

    }
}