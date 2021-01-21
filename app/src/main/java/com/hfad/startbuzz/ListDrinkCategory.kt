package com.hfad.startbuzz

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.ListFragment

class ListDrinkCategory:ListFragment() {

    private lateinit var cursor: Cursor
    private lateinit var db: SQLiteDatabase


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbHelper = activity?.let { StarbuzzDatabaseHelper(it) }
        try {
            dbHelper?.let {
                db = it.readableDatabase
                cursor = db.query(
                    "DRINK",
                    listOf("_id", "NAME").toTypedArray(),
                    null,
                    null,
                    null,
                    null,
                    null
                )
            }
            activity?.let {
                val listAdapter = SimpleCursorAdapter(it,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    listOf("NAME").toTypedArray(),
                    listOf(android.R.id.text1).toIntArray(),
                    0
                )
                setListAdapter(listAdapter)
            }


        }
        catch (e: SQLiteException) {
            activity?.let { Toast.makeText(it, "Database unaviable", Toast.LENGTH_SHORT).show() }
        }

    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val intent = Intent(activity, DrinkActivity::class.java)
        //Отправляем сообщение через intent
        intent.putExtra(DrinkActivity.EXTRA_DRINKNO, id.toInt())
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.let {
            cursor.close()
            db.close()
        }
    }



}


