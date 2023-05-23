package com.yesnet.imagediff

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startbutton = findViewById<Button>(R.id.StartGame)
        startbutton.setOnClickListener{
            val  intent = Intent(this, LevelScreen::class.java)
            startActivity(intent)
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Finish all activities in stack and app closes
        val toast = Toast.makeText(this@StartActivity, "exit the app", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
        Handler(Looper.getMainLooper()).postDelayed({
            kotlin.run {
                finishAffinity()}
        }, 2000)
    }

}