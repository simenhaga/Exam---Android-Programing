package no.kristiania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import no.kristiania.databinding.ActivitySelectImageBinding

class  SelectImageActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivitySelectImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "ReverseImageSearchApp"

        binding.fragmentbutton1.setOnClickListener{
            replaceFragment(SelectImageFragment1())
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

    private fun replaceFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}