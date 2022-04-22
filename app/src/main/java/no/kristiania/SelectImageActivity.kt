package no.kristiania

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
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
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.androidnetworking.AndroidNetworking
import com.bumptech.glide.Glide
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.fasterxml.jackson.databind.ser.Serializers
import com.jacksonandroidnetworking.JacksonParserFactory
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import no.kristiania.databinding.ActivitySelectImageBinding
import okhttp3.OkHttpClient
import java.io.File

class  SelectImageActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectImageBinding
    lateinit var imageView: ImageView
    private var selectedImage: File? = null

    private val GALLERY_REQUEST_CODE = 1234

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivitySelectImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "ReverseImageSearchApp"

        val database = Globals.getDatabase(baseContext)

        val okHttpClient = OkHttpClient().newBuilder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()
        AndroidNetworking.initialize(applicationContext, okHttpClient)
        AndroidNetworking.setParserFactory(JacksonParserFactory())


        binding.fragmentbutton1.setOnClickListener{
            requestStoragePermission()
        }

        binding.fragmentButton2.setOnClickListener{
            selectedImage?.let {
                database.saveImages(StoredImageModel(uri = selectedImage?.toUri().toString()))
                Log.d("Database", "${database.getImages()}")
                apiController.uploadFileToServer(it) { result -> Globals.uploadUrl = result }
            }
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


    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let {
                    launchImageCrop(imageUri)
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
                    var filename = imageUri?.lastPathSegment
                    filename = imageUri?.lastPathSegment?.substringBefore(".")
                    val file = BitmapUtils.bitmapToFile(bitmap, "${filename}.png", this)
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