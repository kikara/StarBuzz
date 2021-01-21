package com.hfad.startbuzz

import android.content.ContentValues
import android.database.sqlite.SQLiteException
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.database.getIntOrNull
import kotlinx.android.synthetic.main.activity_drink.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DrinkActivity : AppCompatActivity() {

    companion object {
        val EXTRA_DRINKNO = "drinkNo"
    }
    private var drinkNo = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)

//        Получение значения напитка из интента
        drinkNo = intent.extras?.get(EXTRA_DRINKNO) as Int

        val favorite = findViewById<CheckBox>(R.id.favorite)


        val starbuzzDatabaseHelper = StarbuzzDatabaseHelper(this)

//        Если не хватает памяти то исключение
        try {
            val db = starbuzzDatabaseHelper.readableDatabase
            val cursor = db?.query(
                "DRINK",
                listOf("NAME","DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE").toTypedArray(),
                "_id = ?",
                arrayOf(drinkNo.toString()),
                null,
                null,
                null)

            cursor?.let {
                if(it.moveToFirst()) {
                    val nameText =it.getString(0)
                    val descriptionText = it.getString(1)
                    val photoId = it.getInt(2)
                    val isFavorite = (it.getInt(3)==1)

//                    Заполнение текст
                    val text:TextView = findViewById(R.id.name)
                    text.text = nameText
//                    Заполнение описания
                    val textDescription = findViewById<TextView>(R.id.description)
                    textDescription.text = descriptionText
//                    Заполнение изображения
                    val photo = findViewById<ImageView>(R.id.photo)
                    photo.setImageResource(photoId)

//                    Заполнение любимого напитка
                    favorite.isChecked = isFavorite

                }
            }
//            Закрыть БД и курсор
            cursor?.close()
            db?.close()

        }
        catch (e: SQLiteException) {
            Toast.makeText(this, "Database unaviable", Toast.LENGTH_SHORT).show()
        }

    }

//    Обработка нажатия CheckBox
        fun onFavoriteClicked(view: View) {

            val drinkValues = ContentValues()
            val favorite = findViewById<CheckBox>(R.id.favorite)
            drinkValues.put("FAVORITE", favorite.isChecked)

//        Получение ссылки на БД и обновление столбца FAVORITE
            val starbuzzDatabaseHelper = StarbuzzDatabaseHelper(this)
            try {
                val db = starbuzzDatabaseHelper.writableDatabase
                db.update(
                        "DRINK",
                        drinkValues,
                        "_id = ?",
                        arrayOf(drinkNo.toString())
                )
                db.close()
            }
            catch (e: SQLiteException) {
                Toast.makeText(this, "Database unaviable", Toast.LENGTH_SHORT).show()
            }
        }
}