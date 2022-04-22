package no.kristiania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.saved_image_fragment1.*
import no.kristiania.databinding.ActivitySavedImageBinding

class SavedImageActivity : AppCompatActivity() {
    lateinit var binding: ActivitySavedImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "ReverseImageSearchApp"

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, SavedImageFragment())
        fragmentTransaction.commit()

        binding.button5.setOnClickListener{
            val i = Intent(this@SavedImageActivity, MainActivity::class.java)
            startActivity(i)
        }

        binding.button3.setOnClickListener{
            val i = Intent(this@SavedImageActivity, SelectImageActivity::class.java)
            startActivity(i)
        }

        binding.button4.setOnClickListener{
            val i = Intent(this@SavedImageActivity, ReverseImageSearchActivity::class.java)
            startActivity(i)
        }

    }
}