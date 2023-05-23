package com.yesnet.imagediff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashLevelFail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_level_fail)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LevelScreen::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}