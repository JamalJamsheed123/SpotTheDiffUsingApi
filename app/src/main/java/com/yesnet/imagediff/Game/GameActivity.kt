package com.yesnet.imagediff

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.yesnet.imagediff.Models.*
import kotlinx.android.synthetic.main.game_level1.*
import kotlinx.android.synthetic.main.item_row.*


class GameActivity : AppCompatActivity() {

    private lateinit var differenceChecker: ImageButton
    private lateinit var differenceChecker1: ImageButton

    private  var  model: GameModelClass?  = null

    private var  boxImageViews1:ArrayList<ImageView>? = null
    private var  boxImageViews2:ArrayList<ImageView>? = null
    var screenOpen = true // logical incorrect
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_level1)

        model = DataManager.instance.getNextLevelGame()


        val layout1 = findViewById<View>(R.id.firstImageLayout) as RelativeLayout
        boxImageViews1 = ArrayList()
        model?.boundingBox?.forEach {
            val lp = RelativeLayout.LayoutParams(it.width*2, it.height*2)
            lp.topMargin = it.y - 20
            lp.marginStart = it.x - 20
            val imageView = ImageView(this@GameActivity) // initialize ImageView
            imageView.visibility = View.GONE
            imageView.layoutParams = lp
            imageView.setImageResource(R.drawable.correct)
            layout1.addView(imageView)
            boxImageViews1?.add(imageView)
        }

        val layout2 = findViewById<View>(R.id.secondImageLayout) as RelativeLayout
        boxImageViews2 = ArrayList()
        model?.boundingBox?.forEach {
            val lp = RelativeLayout.LayoutParams(it.width*2, it.height*2)
            lp.topMargin = it.y - 20
            lp.marginStart = it.x - 20
            val imageView = ImageView(this@GameActivity)
            imageView.visibility = View.GONE
            imageView.layoutParams = lp
            imageView.setImageResource(R.drawable.correct)
            layout2.addView(imageView)
            boxImageViews2?.add(imageView)
        }

        differenceChecker = findViewById(R.id.DifferenceChecker1)
        Glide.with(this)
            .load(model?.imageUrl)
            .placeholder(R.drawable.image_placeholder)
            .into(differenceChecker)

        differenceChecker1 = findViewById(R.id.DifferenceChecker2)
        Glide.with(this)
            .load(model?.differenceImageUrl)
            .placeholder(R.drawable.image_placeholder)
            .into(differenceChecker1)

        handleTouchEvent()

    }

    fun checkAllBoxPass():Boolean{

        val check = model?.boundingBox?.firstOrNull { !it.isPass }?.isPass

        if (check != null)
            return check

        return true

    }

    //sharedPreferences.edit().putInt("IsUnlocked",2).apply()
    fun successUnlockNextLevel(){
        val sharedPreferences = getSharedPreferences("game", Context.MODE_PRIVATE)
        model?.let { sharedPreferences.edit().putInt("IsUnlocked", it.level).apply() }

    }

    var count = 0
    fun returnLifeCount(): Int {
        return count++ // return the updated count
    }

    var num = 0
    fun remainDiffcount(): Int {
        return num++ // return the updated count
    }
    @SuppressLint("ClickableViewAccessibility")
    fun handleTouchEvent(){

        val handleTouch: View.OnTouchListener = object : View.OnTouchListener {
            @SuppressLint("MissingInflatedId")
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                val x = event.x.toInt()
                val y = event.y.toInt()
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> Log.i("TAG", "touched down")
                    MotionEvent.ACTION_MOVE -> Log.i("TAG", "moving: ($x, $y)")
                    MotionEvent.ACTION_UP -> Log.i("TAG", "touched up")
                }

                val screenX = event.x.toInt()
                val screenY = event.y.toInt()

                var anyCorrect = false
                run lit@{
                    model?.boundingBox?.forEach {
                        if (screenX > it.x && screenY > it.y && screenX < it.x + it.width && screenY < it.y + it.height) {
                            it.isPass = true
                            anyCorrect  = true
                            val position = model!!.boundingBox!!.indexOf(it)
                            boxImageViews1?.get(position)?.visibility  = View.VISIBLE
                            boxImageViews2?.get(position)?.visibility = View.VISIBLE
                            val remainingDiff = findViewById<View>(R.id.remainingDifference) as TextView
                            val Number = remainDiffcount()
                            if (Number == 1) {
                                remainingDiff.setText(getString(R.string.remainingDifferce1))
                                remainingDiff.setVisibility(View.VISIBLE)
                            }
                            else if (Number == 3){
                                remainingDiff.setText(getString(R.string.remainingDifferce2))
                                remainingDiff.setVisibility(View.VISIBLE)
                            }
                            else if (Number == 5){
                                remainingDiff.setText(getString(R.string.remainingDifferce3))
                                remainingDiff.setVisibility(View.VISIBLE)
                            }
                            else if (Number == 7){
                                remainingDiff.setText(getString(R.string.remainingDifferce4))
                                remainingDiff.setVisibility(View.VISIBLE)
                            }
                            else if (Number == 9){
                                remainingDiff.setText(getString(R.string.remainingDifferce5))
                                remainingDiff.setVisibility(View.VISIBLE)
                            }
                            return@lit
                        }
                    }
                }


                        if (!anyCorrect) {
                            val wrongPress = findViewById<ImageView>(R.id.wrongPress)
                            wrongPress.setVisibility(View.VISIBLE)
                            Handler().postDelayed(
                                { wrongPress.setVisibility(View.GONE) },
                                (1000).toLong()
                            )
                            //TODO:: Life line handling
                            val rt = findViewById<View>(R.id.ratingBarForLife) as RatingBar
                            rt.setIsIndicator(true)

                            val Count = returnLifeCount()
                            if (Count == 1){
                                rt.rating = rt.numStars - 1f
                            }
                            else if (Count == 3){
                                rt.rating = rt.numStars - 2f
                            }
                            else if (Count == 5){
                                rt.rating = rt.numStars - 3f
                                val toast1 = Toast.makeText(
                                    applicationContext,
                                    "NO LIFE LEFT",
                                    Toast.LENGTH_SHORT
                                )
                                toast1.show()
                                val handler = Handler()
                                handler.postDelayed(Runnable { toast1.cancel() }, 900)

                                if (screenOpen) {
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        val intent =
                                            Intent(this@GameActivity, SplashLevelFail::class.java)
                                        startActivity(intent)
                                        finish()
                                    }, 1000)
                                    screenOpen = false
                                }

                            }

                        }


                val found = checkAllBoxPass()


                if (found){
                    if (screenOpen){
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@GameActivity, SplashLevelComplete::class.java)
                            startActivity(intent)
                            finish()
                        }, 1000)

                        DataManager.instance.markLevelPass()
                        successUnlockNextLevel()
                        screenOpen =  false
                    }
                }

                Log.d("Touch Event", "Image  X, Y " + screenX + "," + screenY)
                return true
            }
        }

        differenceChecker1.setOnTouchListener(handleTouch)
        differenceChecker.setOnTouchListener(handleTouch)

    }

}


//  val levelName = intent.getStringExtra("levelName")
      //  val levelUnlocked = intent.getBooleanExtra("levelUnLocked", false)
     //   model = GameModelClass(levelName,levelUnlocked,null,null,null,1,null)


        //FOR DIFFERENCE 1
        //Initialize bounding box all coordinates

       /* val result = arrayListOf<BoundingBox>()
        val x1 = 525
        val y1 = 270
        val width1 = 50
        val height1 = 50
        //set using data class
        val box1 = BoundingBox(x1, y1, width1, height1)
        result.add(box1)
        //FOR DIFFERENCE 2
        //Initialize bounding box all coordinates
        val x2 = 850
        val y2 = 350
        val width2 = 50
        val height2 = 50
        //set using data class
        val box2 = BoundingBox(x2, y2, width2, height2)
        result.add(box2)

        //FOR DIFFERENCE 3
        //Initialize bounding box all coordinates
        val x3 = 15
        val y3 = 960
        val width3 = 50
        val height3 = 50
        //set using data class
        val box3 = BoundingBox(x3, y3, width3, height3)
        result.add(box3)
        //FOR DIFFERENCE 4
        //Initialize bounding box all coordinates
        val x4 = 840
        val y4 = 680
        val width4 = 50
        val height4 = 50
        //set using data class
        val box4 = BoundingBox(x4, y4, width4, height4)
        result.add(box4)
        //FOR DIFFERENCE 5
        //Initialize bounding box all coordinates
        val x5 = 860
        val y5 = 110
        val width5 = 50
        val height5 = 50
        //set using data class
        val box5 = BoundingBox(x5, y5, width5, height5)
        result.add(box5)
*/


/*
        val handleTouch: View.OnTouchListener = object : View.OnTouchListener {
    //        @SuppressLint("SuspiciousIndentation")
            @SuppressLint("SuspiciousIndentation")
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                val x = event.x.toInt()
                val y = event.y.toInt()
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> Log.i("TAG", "touched down")
                    MotionEvent.ACTION_MOVE -> Log.i("TAG", "moving: ($x, $y)")
                    MotionEvent.ACTION_UP -> Log.i("TAG", "touched up")
                }

                val screenX = event.x.toInt()
                val screenY = event.y.toInt()

        run lit@{
            model?.boundingBox?.forEach {

                if (screenX > it.x && screenY > it.y && screenX < it.x + it.width && screenY < it.y + it.height) {
                    it.isPass = true

                    val toast1 = Toast.makeText(
                        applicationContext,
                        "Correct",
                        Toast.LENGTH_SHORT
                    )
                    toast1.show()
                    val handler = Handler()
                    handler.postDelayed(Runnable { toast1.cancel() }, 500)


                    return@lit

                }

            }
        }
                 val found = checkAllBoxPass()
                    if (found == true){

                        val toast1 = Toast.makeText(
                            applicationContext,
                            "Congratulation User Action Correct",
                            Toast.LENGTH_SHORT
                        )
                        toast1.show()
                        successUnlockNextLevel()
                        val handler = Handler()
                        handler.postDelayed(Runnable { toast1.cancel() }, 500)

                        finish()
                    }
                    else
                    {
                        val toast2 = Toast.makeText(
                            applicationContext, "Please find all Difference", Toast.LENGTH_SHORT
                        )
                        toast2.show()

                        val handler = Handler()
                        handler.postDelayed(Runnable { toast2.cancel() }, 500)
                    }
                    Log.d("Touch Event", "Image  X, Y " + screenX + "," + screenY)
                    return true
                }
            }*/

