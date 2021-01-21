package com.example.scryfallmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MainAdapter(val homeFeed : HomeFeed) : RecyclerView.Adapter<CustomViewHolder>() {

    // numberOfItems
    override fun getItemCount(): Int {
        return homeFeed.cards.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.video_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val cardObject = homeFeed.cards.get(position)
        val view : View = holder.view

        val cardImage : ImageView = view.findViewById(R.id.imageCard)
        Picasso.with(holder.view.context).load(cardObject.image_uris.png).into(cardImage)
    }
}

class CustomViewHolder(val view : View) : RecyclerView.ViewHolder(view) {

}