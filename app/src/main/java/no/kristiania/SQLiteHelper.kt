package no.kristiania

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "imagedb"

        private val STORED_IMAGES = "stored_images"
        private val STORED_ID = "stored_id"
        private val URI = "uri"

        private val STORED_RESULTS = "stored_results"
        private val RESULT_ID = "result_id"
        private val IMAGE_LINK = "image_link"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createStoredTable = (
                "CREATE TABLE $STORED_IMAGES " +
                    "($STORED_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " $URI TEXT)")

        val createResultsTable =(
                "CREATE TABLE $STORED_RESULTS " +
                    "($RESULT_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " $IMAGE_LINK TEXT,")

        db?.execSQL(createStoredTable)
        db?.execSQL(createResultsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $STORED_IMAGES")
        db?.execSQL("DROP TABLE IF EXISTS $STORED_RESULTS")
    }

    fun saveImages (image: StoredImageModel): Long? {
        val db = this.writableDatabase
        val contentValue = ContentValues()

        contentValue.put(URI, image.uri)
        val id = db.insert(STORED_IMAGES, null, contentValue)
        db.close()

        return id
    }

    fun saveImagesFromResult (image: StoredResultsModel): Long? {
        val db = this.writableDatabase
        val contentValue = ContentValues()

        contentValue.put(IMAGE_LINK, image.imageLink)
        val id = db.insert(STORED_RESULTS, null, contentValue)
        db.close()

        return id
    }

    @SuppressLint("Range")
    fun getImages(): List<StoredImageModel> {
        val db = this.readableDatabase
        val result = arrayListOf<StoredImageModel>()
        val statement = "SELECT * FROM $STORED_IMAGES"

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(statement, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(statement)
            return listOf()
        }
        if (cursor.moveToFirst()){
            do {
                result.add(
                    StoredImageModel(
                        cursor.getLong(cursor.getColumnIndex(STORED_ID)),
                        cursor.getString(cursor.getColumnIndex(URI))
                    )
                )
            }while (cursor.moveToNext())
        }
        return result
    }

    fun saveSearchResults (result: StoredResultsModel) {
        val db = this.writableDatabase
        val contentValue = ContentValues()

        contentValue.put(STORED_RESULTS, result.imageLink)
        contentValue.put(STORED_RESULTS, result.storedImageID)
        db.close()
    }

}