package com.example.ispend

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DBHelper(var context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){


    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE $TABLE_NAME (" +
                "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$SPEND_COL REAL," +
                "$SPEND_TYPE VARCHAR(20)," +
                "$SPEND_DATE VARCHAR(50))")

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        // this method is to check if table already exists
//        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
//        onCreate(db)
    }

    // insert data to database
    fun insertValueDB(value: Float, type: String, date: String){
        val values = ContentValues()

        values.apply {
            put(SPEND_COL, value)
            put(SPEND_TYPE, type)
            put(SPEND_DATE, date)
        }

        val db = this.writableDatabase
        var result = db.insert(TABLE_NAME, null, values)
        if(result == (-1).toLong()){
            Toast.makeText(context, "Insert Failed", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    // get data from database
    fun getValueDB(): Cursor?{
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }


    // Delete based on date
    fun deleteData(id: String){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$ID_COL =?", arrayOf(id))
    }


    companion object{
        private val DATABASE_NAME = "iSpend"
        private val DATABASE_VERSION = 1

        val TABLE_NAME = "ListOfSpend"
        val ID_COL = "id"
        val SPEND_COL = "spendValue"
        val SPEND_TYPE = "Type"
        val SPEND_DATE = "spendDate"
    }
}