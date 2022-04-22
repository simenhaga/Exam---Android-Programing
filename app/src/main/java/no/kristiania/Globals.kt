package no.kristiania

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import java.io.Serializable
import java.net.URL
import kotlin.concurrent.thread
import kotlin.random.Random

object Globals {
    val TAG = "Exam"
    var uploadUrl: String? = null

    fun getDatabase(context: Context): SQLiteHelper {
        if(!init){
            init = true
            database = SQLiteHelper(context)
        }
        return database
    }
}

private var init : Boolean = false
private lateinit var database: SQLiteHelper


fun Random.generateRandomString(intRange: IntRange): String {
    var randomString: String = ""
    repeat(intRange.random()){ randomString += ('a'..'z').random().toString() }
    return randomString
}

fun VectorDrawableToBitmap(context: Context, id: Int?, uri: String?) : Bitmap {
    val drawable = (ContextCompat.getDrawable(context!!, id!!) as VectorDrawable)
    val image = Bitmap.createBitmap(
        drawable.getIntrinsicWidth(),
        drawable.getIntrinsicHeight(),
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(image)
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
    drawable.draw(canvas)

    return image
}

fun UriToBitmap(context: Context, id: Int?, uri: String?): Bitmap {
    val image: Bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, Uri.parse(uri))
    return image
}

fun getBitmap(context: Context, id: Int?, uri: String?, decoder: (Context, Int?, String?) -> Bitmap): Bitmap {
    return decoder(context, id, uri)
}