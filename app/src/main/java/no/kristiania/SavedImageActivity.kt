package no.kristiania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SavedImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_image)

        title = "ReverseImageSearchApp"

        val buttonFive: Button = findViewById(R.id.button5)
        buttonFive.setOnClickListener {
            val i = Intent(this@SavedImageActivity, MainActivity::class.java)
            startActivity(i)
        }

        val buttonThree:Button = findViewById(R.id.button3)
        buttonThree.setOnClickListener{
            val i = Intent(this@SavedImageActivity, SavedImageActivity::class.java)
            startActivity(i)
        }

        val buttonFour:Button = findViewById(R.id.button4)
        buttonFour.setOnClickListener{
            val i = Intent(this@SavedImageActivity, ReverseImageSearchActivity::class.java)
            startActivity(i)
        }
    }
}