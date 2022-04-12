package no.kristiania

import android.app.Activity
import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError

import org.json.JSONArray

import com.androidnetworking.interfaces.JSONArrayRequestListener
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    val REQUEST_CODE = 100
    private lateinit var imageView: ImageView
    val uriPathHelper = URIPathHelper()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "ReverseImageSearchApp"
        AndroidNetworking.initialize(getApplicationContext());


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

        AndroidNetworking.get("http://localhost:127.0.0.1/api/v1/imagesearch/bing/{image}")
            .addPathParameter("image", "https://svanemerket.no/content/uploads/2021/09/GettyImages-1082411378-1-scaled.jpg")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    // do anything with response
                    // val json: JSONArray = JSONArray(this)

                    Log.d(Globals.TAG, "Object: " + response.get(1))

                    /*for (index in 0 until json.length()){
                        val imageLink: String = (json.get(index) as JSONObject).get("image_link").toString()

                        Log.d(Globals.TAG, "Image Link: " + imageLink)
                    }*/
                }

                override fun onError(error: ANError) {
                    // handle error
                }
            })

    }


//    private fun openGalleryForImage() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startForResult.launch(Intent.createChooser(intent, "Select Picture"))
//    }
private fun openGalleryForImage() {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    startActivityForResult(intent, REQUEST_CODE)
}

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
                imageView.setImageURI(data?.data) // handle chosen image
            }

        }

    /*val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                var imageUri = result.data?.data.toString()
            }
        }*/
        }


