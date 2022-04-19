package no.kristiania

import android.Manifest
import android.app.Activity
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.bumptech.glide.Glide
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jacksonandroidnetworking.JacksonParserFactory
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import no.kristiania.databinding.ActivitySelectImageBinding
import no.kristiania.databinding.ImageRvLayoutBinding
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
    private val GALLERY_REQUEST_CODE = 1234

    val imageList = ArrayList<ImageApi>()
    private lateinit var imageRV:RecyclerView
    private lateinit var fragmentButton1:FloatingActionButton // ????



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivitySelectImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "ReverseImageSearchApp"

        //RVReversedImageSearch("https://gtl-bucket.s3.amazonaws.com/dc603e718d0c4eaf8216bb5b685195d3.jpg")

        // Init views
        imageRV = findViewById(R.id.apiRecyclerView)
        //fragmentButton1 = findViewById(R.id.fragmentbutton1)

        imageRV.layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)

        val okHttpClient = OkHttpClient().newBuilder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()
        AndroidNetworking.initialize(applicationContext, okHttpClient)
        AndroidNetworking.setParserFactory(JacksonParserFactory())


        binding.fragmentbutton1.setOnClickListener{
            //replaceFragment(SelectImageFragment1())
            //MainActivity().requestPermissionAndOpenGallery
            requestStoragePermission()
        }

        binding.fragmentButton2.setOnClickListener{
            selectedImage?.let {
                apiController.uploadFileToServer(it)
            }
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


    private fun openGalleryForImage() {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT
        //startForResult.launch(Intent.createChooser(intent, "Select Picture"))
        requestStoragePermission()
        openGalleryLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private var selectedImage: File? = null

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageUri: Uri? = result.data?.data
                imageView.setImageURI(imageUri)
                imageUri?.let {
                    launchImageCrop(imageUri)
                    setImage(imageUri)
                    }
                }
            }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            GALLERY_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let { uri ->
                        launchImageCrop(uri)
                    }
                }
                else{
                    Log.e(Globals.TAG, "Image selection error: Couldn't select that image from memory." )
                }
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val imageUri: Uri? = result.uri
                    val bitmap = BitmapUtils.getBitmap(
                        this, null, imageUri.toString(), ::UriToBitmap
                    )
                    val file = BitmapUtils.bitmapToFile(bitmap, "cropped.png", this)
                    selectedImage = file
                    Log.d(Globals.TAG, "file: $selectedImage")
                    setImage(result.uri)
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Log.e(Globals.TAG, "Crop error: ${result.getError()}" )
                }
            }
        }
    }

    private fun launchImageCrop(imageUri: Uri){
        CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.RECTANGLE) // default is rectangle
            .start(this)
    }

    private fun setImage(uri: Uri){
        Glide.with(this)
            .load(uri)
            .into(imageView)
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