package no.kristiania

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object BitmapUtils {
    fun getBitmap(context: Context, id: Int?, uri: String?, decoder: (Context, Int?, String?) -> Bitmap): Bitmap {
        return decoder(context, id, uri)
    }

    fun bitmapToByteArray(bitmap : Bitmap) : ByteArray{
        val outputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }


    //FROM https://stackoverflow.com/questions/7769806/convert-bitmap-to-file
    fun bitmapToFile(bitmap : Bitmap, filename : String, context: Context) : File{

        val file = File(context.cacheDir, filename)
        file.createNewFile()

        val bitmapdata = bitmapToByteArray(bitmap)

        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(bitmapdata)
        fileOutputStream.flush()
        fileOutputStream.close()

        return file
    }

    fun UriToBitmap(context: Context, id: Int?, uri: String?): Bitmap {
        val image: Bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, Uri.parse(uri))
        return image
    }
}