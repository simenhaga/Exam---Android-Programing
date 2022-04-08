package no.kristiania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "ReverseImageSearchApp"


    val buttonThree:Button = findViewById(R.id.button3)
    buttonThree.setOnClickListener{
        val intent = Intent(this@MainActivity, SelectImageActivity::class.java)
        startActivity(intent)
    }

    val buttonFour:Button = findViewById(R.id.button4)
    buttonFour.setOnClickListener{
        val intent = Intent(this@MainActivity, ReverseImageSearchActivity::class.java)
        startActivity(intent)
    }

    val buttonFive:Button = findViewById(R.id.button5)
        buttonFive.setOnClickListener{
            val intent = Intent(this@MainActivity, SavedImageActivity::class.java)
            startActivity(intent)
        }

    }



}