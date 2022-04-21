package no.kristiania

import android.content.ContentValues
import android.content.Context
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
                    " $IMAGE_LINK TEXT," +
                    " $STORED_ID INTEGER," +
                    " FOREIGN KEY($STORED_ID) REFERENCES $STORED_IMAGES($STORED_ID))")

        db?.execSQL(createStoredTable)
        db?.execSQL(createResultsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $STORED_IMAGES")
        db?.execSQL("DROP TABLE IF EXISTS $STORED_RESULTS")
    }

    fun saveImages (image: StoredImageModel) {
        val db = this.writableDatabase
        val contentValue = ContentValues()

        contentValue.put(STORED_IMAGES, image.uri)
        db.close()
    }

    fun saveSearchResults (result: StoredResultsModel) {
        val db = this.writableDatabase
        val contentValue = ContentValues()

        contentValue.put(STORED_RESULTS, result.imageLink)
        contentValue.put(STORED_RESULTS, result.storedImageID)
        db.close()
    }

}