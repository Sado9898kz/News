package com.beeline.news

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val position = arguments?.getInt("p") ?: 0

        Realm.getDefaultInstance().executeTransaction { realm ->
            val list = realm.where(ArticleSave::class.java).findAll()
            tv_date.text = list[position]!!.publishedAt.format()
            Picasso.with(context)
                .load(list[position]!!.urlToImage)
                .placeholder(R.drawable.image_loade)
                .error(R.drawable.ic_launcher_foreground)
                .into(iv_image_news)
            tv_title_news.text = list[position]!!.title
            tv_content_news.text = list[position]!!.content
            tv_link.text = list[position]!!.url
            Linkify.addLinks(tv_link,Linkify.WEB_URLS)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}