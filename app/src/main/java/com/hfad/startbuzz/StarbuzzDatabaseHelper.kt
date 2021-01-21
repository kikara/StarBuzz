package com.hfad.startbuzz

import android.content.ClipDescription
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StarbuzzDatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "starbuzz"
        private const val DATABASE_VERSION = 2
    }


    override fun onCreate(db: SQLiteDatabase?) {
        updateMyDatabase(db, 0, DATABASE_VERSION)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        updateMyDatabase(db, oldVersion, newVersion)
    }

    private fun updateMyDatabase(db: SQLiteDatabase?, old: Int, new: Int) {
        if (old<1) {
            //Создаем столбцы для таблицы SQL
            db?.execSQL("CREATE TABLE DRINK (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAME TEXT, " +
                    "DESCRIPTION TEXT, " +
                    "IMAGE_RESOURCE_ID INTEGER);")
            //Заполняем таблицу
            insertDrink(db, "Latte", "Espresso and steamed milk", R.drawable.latte)
            insertDrink(db, "Cappuccino", "Espresso, hot milk and steamed-milk foam",
                    R.drawable.cappuccino)
            insertDrink(db, "Filter", "Our best drip coffee", R.drawable.filter)
        }
        if(old<2){
            db?.execSQL("ALTER TABLE DRINK ADD COLUMN FAVORITE NUMERIC;")
        }
    }

    private fun insertDrink(db: SQLiteDatabase?, name:String, description: String, resourceId:Int) {
        ContentValues().apply {
            put("NAME", name)
            put("Description", description)
            put("IMAGE_RESOURCE_ID", resourceId)
            db?.insert("DRINK", null,this)
        }
    }


}