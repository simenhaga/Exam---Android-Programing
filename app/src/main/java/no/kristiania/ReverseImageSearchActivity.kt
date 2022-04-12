package no.kristiania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import no.kristiania.databinding.ActivityReverseImageSearchBinding

class ReverseImageSearchActivity : AppCompatActivity() {
    lateinit var binding: ActivityReverseImageSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReverseImageSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "ReverseImageSearchApp"

        binding.fragmentButton4.setOnClickListener{
            replaceFragment(ReverseImageSearchFragment())
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