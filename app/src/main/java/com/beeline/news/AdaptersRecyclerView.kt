package com.beeline.news

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.realm.RealmResults


class RecyclerView(private val items: RealmResults<ArticleSave>) : RecyclerView.Adapter<RecHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recyclerview_item, parent, false)

        return RecHolder(view)
    }

    override fun onBindViewHolder(holder: RecHolder, position: Int) {
        val item = items[position]!!
        holder.bind(item)
        holder.itemView.setOnClickListener {
            val ac = it.context as AppCompatActivity
            val intent = Intent(ac, NewsActivity::class.java)
            intent.putExtra("position",position)
            ac.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class RecHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title = itemView.findViewById<TextView>(R.id.tv_title)
    private val description = itemView.findViewById<TextView>(R.id.tv_description)
    private val urlImage = itemView.findViewById<ImageView>(R.id.iv_urlImage)
    private val context = view.context

    fun bind(item: ArticleSave) {
        title.text = item.title
        description.text = item.description

        Picasso.with(context)
            .load(item.urlToImage)
            .placeholder(R.drawable.image_loade)
            .error(android.R.drawable.stat_notify_error)
            .into(urlImage)

    }
}

