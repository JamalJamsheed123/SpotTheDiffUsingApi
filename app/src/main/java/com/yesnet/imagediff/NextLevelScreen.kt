package com.yesnet.imagediff

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_next_level_screen.view.*

class NextLevelScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_level_screen)


        val gotoHome = findViewById<Button>(R.id.gotoHomeScreen)
        gotoHome.setOnClickListener{
            val  intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()
        }

        val gotoNextLevelScreen = findViewById<Button>(R.id.nextLevel)
        gotoNextLevelScreen.setOnClickListener{

            val context = this
        //    DataManager.instance.getList(context)

          //  DataManager.instance.markLevelPass(0)
            val  intent = Intent(context, GameActivity::class.java)
            startActivity(intent)
            finish()

        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent (this@NextLevelScreen, LevelScreen::class.java)
        startActivity(intent)
    }
}