package no.kristiania

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonThree = findViewById<Button>(R.id.button3)
        val buttonFour = findViewById<Button>(R.id.button4)
        val buttonFive = findViewById<Button>(R.id.button5)

    }
}