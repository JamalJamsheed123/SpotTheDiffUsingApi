package com.yesnet.imagediff

import android.content.Context
import com.google.gson.Gson
import com.yesnet.imagediff.Models.GameModel
import com.yesnet.imagediff.Models.GameModelClass

class DataManager {

    companion object {
        val instance:DataManager by lazy {
            DataManager()
        }
        private const val PREFS_NAME = "SharedPreferences"
        private const val LIST_PREFS_NAME = "ListPreferences"
    }

     private var gameModel: GameModel?
     var currentLevel = -1
    init {
        gameModel = null
    }

    fun getGameModel(): GameModel? {
        return gameModel
    }

    fun getList(context: Context): GameModel? {
        val prefs = context.getSharedPreferences(DataManager.PREFS_NAME, Context.MODE_PRIVATE)
        val listString = prefs.getString(Companion.LIST_PREFS_NAME, "0") ?: ""
        gameModel = Gson().fromJson(listString, GameModel::class.java)
        return gameModel
    }

    fun saveList(context: Context,levels:GameModel)  {
        val sharedPreferences = context.getSharedPreferences(DataManager.PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val jsonList = Gson().toJson(levels)
        editor.putString(Companion.LIST_PREFS_NAME, jsonList)
        editor.apply()
        gameModel = levels
    }

    fun markLevelPass(): GameModel? {
        val nextLevel = gameModel?.game?.firstOrNull{ item -> item.level == currentLevel + 1}
        if(nextLevel != null){
            currentLevel = nextLevel.level
            nextLevel.isUnlocked = true
            return gameModel
        }
        return  null
    }

    fun getNextLevelGame() : GameModelClass? {
        val levelObject = gameModel?.game?.firstOrNull{ item -> item.level == currentLevel }
        if(levelObject != null){
            return levelObject
        }
        return  null
    }

    fun  getLevelObject(level: Int) : GameModelClass? {
        val levelObject = gameModel?.game?.firstOrNull{ item -> item.level == level }
        return levelObject
    }
}



