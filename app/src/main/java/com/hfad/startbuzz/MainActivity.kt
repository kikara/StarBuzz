package com.hfad.startbuzz

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener

/*
* Торбогошев Артур Адынарович
*
*/
class MainActivity : AppCompatActivity() {

    private lateinit var db: SQLiteDatabase
    private lateinit var cursor: Cursor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupOptionsListView()
        setupFavoritesListView()
    }

    private fun setupOptionsListView() {
        //Создаем слушатель itemClickListener
        val itemClickListener = OnItemClickListener { lisView, itemView, position, id ->
            if (position == 0) {
                val intent = Intent(this, DrinkCategoryActivity::class.java)
                startActivity(intent)
            }
        }

        //Связываем слушатель с ListView
        val listView: ListView = findViewById(R.id.list_options)
        listView.onItemClickListener = itemClickListener
    }

    private fun setupFavoritesListView() {
        val favoritesListView = findViewById<ListView>(R.id.list_favorites)
        val starbuzzDatabaseHelper = StarbuzzDatabaseHelper(this)

        try {
            db = starbuzzDatabaseHelper.readableDatabase
            cursor = db.query(
                "DRINK",
                listOf("_id", "NAME").toTypedArray(),
                "FAVORITE = 1",
                null,
                null,
                null,
                null
            )

            val cursorAdapter = SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                cursor,
                listOf("NAME").toTypedArray(),
                listOf(android.R.id.text1).toIntArray(),
                0)

            favoritesListView.adapter = cursorAdapter
        }
        catch (e: SQLiteException) {
            Toast.makeText(this, "Database unaviable", Toast.LENGTH_SHORT).show()
        }

        val itemClickListener = OnItemClickListener { listView, itemView, position, id ->
                val intent = Intent(this, DrinkActivity::class.java)
                intent.putExtra(DrinkActivity.EXTRA_DRINKNO, id.toInt())
                startActivity(intent)
        }

        favoritesListView.onItemClickListener = itemClickListener
    }

    override fun onRestart() {
        super.onRestart()
        val newCursor = db.query(
                "DRINK",
                listOf("_id", "NAME").toTypedArray(),
                "FAVORITE = 1",
                null,
                null,
                null,
                null
        )
        val listFavorites = findViewById<ListView>(R.id.list_favorites)
        val adapter = listFavorites.adapter as CursorAdapter
        adapter.changeCursor(newCursor)
        cursor = newCursor
    }
    override fun onDestroy() {
        super.onDestroy()
        db.close()
        cursor.close()
    }
}