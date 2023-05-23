package com.yesnet.imagediff.Models
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class GameModel(
    @SerializedName("game")
    val game: ArrayList<GameModelClass>
): Serializable

data class GameModelClass (
    @SerializedName("Name")
    var Name: String? = null,
    @SerializedName("isUnlocked")
    var isUnlocked : Boolean? = null,
    @SerializedName("imageUrl")
    var imageUrl: String? = null,
    @SerializedName("thumbnailUrl")
    var thumbnailUrl: String? = null,
    @SerializedName("differenceImageUrl")
    var differenceImageUrl: String? = null,
    @SerializedName("level")
    var level: Int,
    @SerializedName("boundingBox")
    var boundingBox: ArrayList<Box>?
    ): Serializable

data class Box (
    var x: Int,
    var y: Int,
    var width: Int,
    var isPass: Boolean = false,
    var height: Int,

): Serializable