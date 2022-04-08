package no.kristiania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ReverseImageSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reverse_image_search)
        title = "ReverseImageSearchApp"

        val buttonFour:Button = findViewById(R.id.button4)
        buttonFour.setOnClickListener{
            val i = Intent(this@ReverseImageSearchActivity, MainActivity::class.java)
            startActivity(i)
        }

        val buttonThree:Button = findViewById(R.id.button3)
        buttonThree.setOnClickListener{
            val i = Intent(this@ReverseImageSearchActivity, SelectImageActivity::class.java)
            startActivity(i)
        }

        val buttonFive:Button = findViewById(R.id.button5)
        buttonFive.setOnClickListener{
            val i = Intent(this@ReverseImageSearchActivity, SavedImageActivity::class.java)
            startActivity(i)
        }

    }
}