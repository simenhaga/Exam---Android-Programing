package no.kristiania

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import com.androidnetworking.AndroidNetworking


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "ReverseImageSearchApp"
        AndroidNetworking.initialize(this)


        val buttonThree: Button = findViewById(R.id.button3)
        val buttonFour: Button = findViewById(R.id.button4)
        val buttonFive: Button = findViewById(R.id.button5)

        buttonThree.setOnClickListener {
            val intent = Intent(this@MainActivity, SelectImageActivity::class.java)
            startActivity(intent)
        }

        buttonFour.setOnClickListener {
            val intent = Intent(this@MainActivity, ReverseImageSearchActivity::class.java)
            startActivity(intent)
        }

        buttonFive.setOnClickListener {
            val intent = Intent(this@MainActivity, SavedImageActivity::class.java)
            startActivity(intent)
        }

    }

}