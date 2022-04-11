package no.kristiania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import no.kristiania.databinding.ActivitySavedImageBinding

class SavedImageActivity : AppCompatActivity() {
    lateinit var binding: ActivitySavedImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "ReverseImageSearchApp"

        binding.fragmentButton3.setOnClickListener{
            replaceFragment(SavedImageFragment())
        }

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

    private fun replaceFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

}