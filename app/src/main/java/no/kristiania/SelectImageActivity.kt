package no.kristiania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import no.kristiania.databinding.ActivityMainBinding

class  SelectImageActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_select_image)
        title = "ReverseImageSearchApp"



        val buttonThree:Button = findViewById(R.id.button3)
        val buttonFour:Button = findViewById(R.id.button4)
        val buttonFive:Button = findViewById(R.id.button5)


        buttonThree.setOnClickListener {
            val i = Intent(this@SelectImageActivity, MainActivity::class.java)
            startActivity(i)
       }

        buttonFour.setOnClickListener{
            val i = Intent(this@SelectImageActivity, ReverseImageSearchActivity::class.java)
            startActivity(i)
        }

        buttonFive.setOnClickListener{
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