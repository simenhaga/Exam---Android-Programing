package no.kristiania

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError

import org.json.JSONArray

import com.androidnetworking.interfaces.JSONArrayRequestListener
//import com.facebook.stetho.okhttp3.StethoInterceptor
import org.json.JSONObject
import okhttp3.OkHttpClient
import com.jacksonandroidnetworking.JacksonParserFactory
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    val REQUEST_CODE = 100
    private lateinit var imageView: ImageView
    val uriPathHelper = URIPathHelper()


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "ReverseImageSearchApp"

        getReversedImage("http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=https://svanemerket.no/content/uploads/2021/09/GettyImages-1082411378-1-scaled.jpg")

        val buttonThree: Button = findViewById(R.id.button3)
        val buttonFour: Button = findViewById(R.id.button4)
        val buttonFive: Button = findViewById(R.id.button5)

        buttonThree.setOnClickListener {
            /*val intent = Intent(this@MainActivity, SelectImageActivity::class.java)
            startActivity(intent)*/
            openGalleryForImage()
        }

        buttonFour.setOnClickListener {
            val intent = Intent(this@MainActivity, ReverseImageSearchActivity::class.java)
            startActivity(intent)
        }

        buttonFive.setOnClickListener {
            val intent = Intent(this@MainActivity, SavedImageActivity::class.java)
            startActivity(intent)
        }


    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getReversedImage(url: String) {
        val url = URL(url)

        thread {
            with(url.openConnection() as HttpURLConnection) {

                inputStream.bufferedReader().lines().forEach {
                    //Log.d(Globals.TAG, it)

                    val json: JSONArray = JSONArray(it)

                    for (index in 0 until json.length()) {
                        val thumbnailLink: String =
                            (json.get(index) as JSONObject).get("thumbnail_link").toString()
                        val imageLink: String =
                            (json.get(index) as JSONObject).get("image_link").toString()

                        Log.i(Globals.TAG, "Image Link: " + imageLink + "\n")
                    }
                }
            }
        }
    }


        private fun openGalleryForImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startForResult.launch(Intent.createChooser(intent, "Select Picture"))
    }

        @RequiresApi(Build.VERSION_CODES.N)
        val startForResult =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            Log.d(Globals.TAG, "imgUri: " + imageUri)
            //val uri = Uri.parse(imageUri.toString())
            val filePath = uriPathHelper.getPath(this, imageUri!!)
            getReversedImage("http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=${filePath}")
            Log.d(Globals.TAG, "FilePath: " + filePath)
        }
    }
}

/*    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 3)
    }
 */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE /*!= null*/) {
                val test: String = imageView.setImageURI(data?.data).toString()
                //Uri selectedImage = data?.data()
                //ImageView imageView = findViewById(R.layout.activity_main)
                //imageView.setImageURI(data?.data) // handle chosen image
                //var imageUri = result.data?.data.toString()
                Log.d(Globals.TAG, "imgUri: " + test)
            }

        }*/