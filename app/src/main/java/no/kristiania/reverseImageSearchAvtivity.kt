package no.kristiania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class reverseImageSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_image)
        title = "ReverseImageSearchApp"

        val buttonFour: Button = findViewById(R.id.button4)
        buttonFour.setOnClickListener {
            val i = Intent(this@reverseImageSearchActivity, MainActivity::class.java)
            startActivity(i)
        }
    }
}