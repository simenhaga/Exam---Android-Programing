package no.kristiania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import no.kristiania.databinding.ActivityReverseImageSearchBinding
import no.kristiania.databinding.ImageRvLayoutBinding
import java.io.File

class ReverseImageSearchActivity : AppCompatActivity() {
    lateinit var binding: ActivityReverseImageSearchBinding
    lateinit var saveBtnBinding: ImageRvLayoutBinding
    private var imageResult: File? = null

    private lateinit var imageRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReverseImageSearchBinding.inflate(layoutInflater)
        saveBtnBinding = ImageRvLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "ReverseImageSearchApp"
        val database = Globals.getDatabase(baseContext)

        // Init views
        imageRV = binding.apiRecyclerView

        imageRV.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        binding.fragmentButton4.setOnClickListener{
            Toast.makeText(this@ReverseImageSearchActivity, "Doing reversed image search on Bing...", Toast.LENGTH_LONG).show()
            Globals.uploadUrl?.let {
                apiController.reversedImageSearchBing(it) { imageList ->
                    imageRV.adapter = ImageAdapter(this@ReverseImageSearchActivity, imageList)
                }
            }
        }

        saveBtnBinding.saveImageBtn.setOnClickListener{
            imageResult?.let {
                database.saveImagesFromResult(StoredResultsModel(imageLink = imageResult?.toUri().toString()))
                Log.d("Database", "${database.getImages()}")
                apiController.uploadFileToServer(it) { result -> Globals.uploadUrl = result }
            }
        }

        binding.fragmentButton6.setOnClickListener{
            Globals.uploadUrl?.let {
                apiController.reversedImageSearchGoogle(it) { imageList ->
                    imageRV.adapter = ImageAdapter(this@ReverseImageSearchActivity, imageList)
                }
            }
        }

        binding.fragmentButton5.setOnClickListener{
            Globals.uploadUrl?.let {
                apiController.reversedImageSearchTinyeye(it) { imageList ->
                    imageRV.adapter = ImageAdapter(this@ReverseImageSearchActivity, imageList)
                }
            }
        }

        binding.button4.setOnClickListener{
            val i = Intent(this@ReverseImageSearchActivity, MainActivity::class.java)
            startActivity(i)
        }

        binding.button3.setOnClickListener{
            val i = Intent(this@ReverseImageSearchActivity, SelectImageActivity::class.java)
            startActivity(i)
        }

        binding.button5.setOnClickListener{
            val i = Intent(this@ReverseImageSearchActivity, SavedImageActivity::class.java)
            startActivity(i)
        }
    }
}