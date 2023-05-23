package com.yesnet.imagediff.Models

import android.util.Base64
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.yesnet.imagediff.R
import kotlinx.android.synthetic.main.item_row.view.*

class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val leveNumberTextView = itemView.levelNumber
    val levelImageView: ImageView = itemView.findViewById(R.id.levelImage)
   // val levelThumbnailView: ImageView = itemView.findViewById(R.id.thumbnail)

    //val diffImageView: ImageButton = itemView.findViewById(R.id.DifferenceChecker2)
    val  levelLockIconImageView = itemView.islock


    fun bind(level: GameModelClass) {


        if (level.isUnlocked == true) {
           levelLockIconImageView.setImageResource(R.drawable.play)
        }
        else {
            levelLockIconImageView.setImageResource(R.drawable.lockicon)
        }
    }
}