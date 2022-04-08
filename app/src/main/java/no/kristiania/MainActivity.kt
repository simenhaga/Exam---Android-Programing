package no.kristiania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "ReverseImageSearchApp"

        //val buttonThree = findViewById<Button>(R.id.button3)
        //val buttonFour = findViewById<Button>(R.id.button4)
        //val buttonFive = findViewById<Button>(R.id.button5)

    val buttonThree:Button = findViewById(R.id.button3)
    buttonThree.setOnClickListener{
        val intent = Intent(this@MainActivity, selectImageActivity::class.java)
        startActivity(intent)
    }

    val buttonFour:Button = findViewById<Button>(R.id.button4)
    buttonFour.setOnClickListener{
        val intent = Intent(this@MainActivity, reverseImageSearchActivity::class.java)
        startActivity(intent)
    }


    }



}