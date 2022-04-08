package no.kristiania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SelectImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_image)
        title = "ReverseImageSearchApp"

        val buttonThree:Button = findViewById(R.id.button3)
        buttonThree.setOnClickListener {
            val i = Intent(this@SelectImageActivity, MainActivity::class.java)
            startActivity(i)
        }

        val buttonFour:Button = findViewById(R.id.button4)
        buttonFour.setOnClickListener{
            val i = Intent(this@SelectImageActivity, ReverseImageSearchActivity::class.java)
            startActivity(i)
        }

        val buttonFive:Button = findViewById(R.id.button5)
        buttonFive.setOnClickListener{
            val i = Intent(this@SelectImageActivity, SavedImageActivity::class.java)
            startActivity(i)
        }
    }
}