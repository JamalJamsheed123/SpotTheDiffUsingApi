package com.yesnet.imagediff.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yesnet.imagediff.Models.GameModelClass
import com.yesnet.imagediff.Models.GameViewHolder
import com.yesnet.imagediff.R


class GameAdapter(
    private val productlist: ArrayList<GameModelClass>


    ): RecyclerView.Adapter<GameViewHolder>()
{
    var onItemClick: ((GameModelClass) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_row,
            parent,false)

        return GameViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {

        val level = productlist[position]
        holder.bind(level)

        // Get data from JSON File
        holder.leveNumberTextView.text = level.Name

        Glide.with(holder.itemView)
            .load(level.thumbnailUrl)
            .placeholder(R.drawable.image_placeholder)
            .into(holder.levelImageView)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(level)

        }
    }
    override fun getItemCount(): Int{
         return productlist.size

    }

}