package no.kristiania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import no.kristiania.databinding.ActivityReverseImageSearchBinding

class ReverseImageSearchActivity : AppCompatActivity() {
    lateinit var binding: ActivityReverseImageSearchBinding

    private lateinit var imageRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReverseImageSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "ReverseImageSearchApp"

        // Init views
        imageRV = findViewById(R.id.apiRecyclerView)

        imageRV.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        binding.fragmentButton4.setOnClickListener{
            Toast.makeText(this@ReverseImageSearchActivity, "Doing reversed image search on Bing...", Toast.LENGTH_LONG).show()
            Globals.uploadUrl?.let {
                apiController.reversedImageSearchBing(it) { imageList ->
                    imageRV.adapter = ImageAdapter(this@ReverseImageSearchActivity, imageList)
                }
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

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}