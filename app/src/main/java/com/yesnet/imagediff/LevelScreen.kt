package com.yesnet.imagediff

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.yesnet.imagediff.Adapter.GameAdapter
import com.yesnet.imagediff.Models.GameModel
import com.yesnet.imagediff.Models.GameModelClass
import com.yesnet.imagediff.Services.DestinationServices
import com.yesnet.imagediff.Services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_level.*
import kotlinx.android.synthetic.main.item_row.*
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LevelScreen : AppCompatActivity(){

    private lateinit var adapter: GameAdapter
    private lateinit var recyclerView: RecyclerView
    var levelList: ArrayList<GameModelClass>? = null
  //  private var gameModel: GameModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)
        loadDestination()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {

        if (levelList != null)
        {
            updateLevelList()
            adapter.notifyDataSetChanged()
        }
        super.onResume()
    }

    override fun onPause() {


        super.onPause()
    }

    fun isLevelUnlocked(level: Int): Boolean{
        val sharedPreferences = getSharedPreferences("game",Context.MODE_PRIVATE)
        val unlockLevel = sharedPreferences.getInt("IsUnlocked",-1)

        // unlockLevel = A
        // Level = B

        //  A < B -> Fail = Condition 1
        //  A > B -> Pass = Condition 2
        // A = B    --> Pass = Condition 3
        // B > A    ----> Fail = Condition 4

        // unlockLevel pichale level hoga hamesah
        if ( unlockLevel >= level || level - unlockLevel == 1 ) { // Pass: Condition 2 +  Condition 3
            return  true // ye maine check kar k unlock kia
        }


      /*  if(level == 1 ){
            return  true // ye maine forcefully unlock kia kyn hame level 1 hamesha unlock chye
        }*/

        return false
    }


   private fun setupLevelAdaptor(){

        try {

            if (DataManager.instance.getGameModel() == null){
                DataManager.instance.getList(this)
            }

            // set the LayoutManager For this Recyclerview use

            val LayoutManager = LinearLayoutManager(this)
            recyclerView = findViewById(R.id.levelListView)
            recyclerView.layoutManager = LayoutManager

            DataManager.instance.getGameModel()?.game?.let {
                adapter = GameAdapter(it)
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = adapter
                levelList = it
                updateLevelList()

            }

            adapter.onItemClick = {

                if (it.isUnlocked == true) {
                    val intent1 = Intent(this, GameActivity::class.java)
                    DataManager.instance.currentLevel = it.level
                    startActivity(intent1)
                } else {
                    it.isUnlocked != true
                }
            }
        }
        catch (e: JSONException){

            e.printStackTrace()

        }
    }

    private fun updateLevelList(){
        levelList?.forEach {
            it.isUnlocked = isLevelUnlocked(it.level)

        }
    }
    private fun loadDestination() {

        val destinationService = ServiceBuilder.buildService(DestinationServices::class.java)

        val requestCall = destinationService.getDestinationList()

        requestCall.enqueue(object: Callback<GameModel> {

            override fun onResponse(call: Call<GameModel>,
                                    response: Response<GameModel>) {
                if (response.isSuccessful) {
                    val gameModel = response.body()!!
                    DataManager.instance.saveList(this@LevelScreen, gameModel)
                    setupLevelAdaptor()
                }

            }

            override fun onFailure(call: Call<GameModel>, t: Throwable) {

                Log.d("MainActivity","onFailure: "+t.message)

            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent (this@LevelScreen, StartActivity::class.java)
        startActivity(intent)
    }
    

}
