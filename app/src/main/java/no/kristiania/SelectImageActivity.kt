package no.kristiania

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jacksonandroidnetworking.JacksonParserFactory
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import no.kristiania.databinding.ActivitySelectImageBinding
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class  SelectImageActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectImageBinding
    lateinit var imageView: ImageView
    private val pickImage = 100

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivitySelectImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "ReverseImageSearchApp"

        val okHttpClient = OkHttpClient().newBuilder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()
        AndroidNetworking.initialize(applicationContext, okHttpClient)
        AndroidNetworking.setParserFactory(JacksonParserFactory())


        getReversedImage("http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=https://svanemerket.no/content/uploads/2021/09/GettyImages-1082411378-1-scaled.jpg")

        binding.fragmentbutton1.setOnClickListener{
            //replaceFragment(SelectImageFragment1())
            //MainActivity().requestPermissionAndOpenGallery
            requestStoragePermission()
        }

        binding.fragmentButton2.setOnClickListener{
            replaceFragment(SelectImageFragment2())
        }

        binding.button3.setOnClickListener{
            val i = Intent(this@SelectImageActivity, MainActivity::class.java)
            startActivity(i)
        }

        binding.button4.setOnClickListener{
            val i = Intent(this@SelectImageActivity, ReverseImageSearchActivity::class.java)
            startActivity(i)
        }

        binding.button5.setOnClickListener{
            val i = Intent(this@SelectImageActivity, SavedImageActivity::class.java)
            startActivity(i)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getReversedImage(url: String) {
        val url = URL(url)

        thread {
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"

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

    private fun postFileToServer(file: File){
        AndroidNetworking.post("http://api-edu.gtl.ai/api/v1/imagesearch/upload")
            .addFileBody(file) // posting any type of file
            .setTag("test")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    // do anything with response
                    Log.d(Globals.TAG, "Response: $response")

                }

                override fun onError(error: ANError) {
                    // handle error
                }
            })
    }

    private fun uploadFileToServer(file: File) {
        AndroidNetworking.upload("http://api-edu.gtl.ai/api/v1/imagesearch/upload")
            .addMultipartFile("image", file)
            //.addMultipartParameter("key", "value")
            .addHeaders("Content-Type", "image/png")
            .setTag("uploadTest")
            .setPriority(Priority.HIGH)
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes ->
                // do anything with progress
            }
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    // do anything with response
                    Log.d(Globals.TAG, "Response: $response")
                }

                override fun onError(error: ANError) {
                    // handle error
                }
            })
    }

    private fun openGalleryForImage() {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT
        //startForResult.launch(Intent.createChooser(intent, "Select Picture"))
        requestStoragePermission()
        openGalleryLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageUri: Uri? = result.data?.data
                imageView.setImageURI(imageUri)
                imageUri?.let {
                    URIPathHelper.getPath(this, it)?.let { path ->
                        val file = File(path)
                        postFileToServer(file)
                        //uploadFileToServer(file)
                        Log.d(Globals.TAG, "file: $file")
                    }
                }
            }
        }

    //================ PERMISSION HANDLING ===================

    val requestPermissionAndOpenGallery: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                imageView = findViewById(R.id.imageView)
                if (isGranted) {
                    Toast.makeText(
                        this@SelectImageActivity,
                        "Permission granted, you can now read storage files",
                        Toast.LENGTH_LONG
                    ).show()

                    val pickIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)

                } else {
                    if (permissionName == Manifest.permission.READ_EXTERNAL_STORAGE) {
                        Toast.makeText(
                            this@SelectImageActivity,
                            "Ooops, you've denied the permission",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }
        }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            showPermissionDeniedDialog()
        } else {
            requestPermissionAndOpenGallery.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }

    //Inform user that they haven't given the correct permissions
    private fun showPermissionDeniedDialog() {

        AlertDialog.Builder(this)
            .setTitle("PERMISSION DENIED")
            .setMessage("Permission is denied. Please allow permissions from App Settings")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .create().show()
    }



    private fun replaceFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }


}