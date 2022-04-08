package no.kristiania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class selectImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_image)
        title = "ReverseImageSearchApp"

        val buttonThree: Button = findViewById(R.id.button3)
        buttonThree.setOnClickListener {
            val i = Intent(this@selectImageActivity, MainActivity::class.java)
            startActivity(i)
        }
    }
}