package com.beeline.news

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_list_news.*
import kotlinx.coroutines.*

class NewsListFragment : Fragment(){

    lateinit var recyclerView: RecyclerView
    lateinit var job: Job

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerView_posts)
        updateNews()
        showRecView()

        swipeRefreshLayout.setOnRefreshListener {
            updateNews()
            showRecView()
            swipeRefreshLayout.isRefreshing = false
        }
        super.onViewCreated(view, savedInstanceState)
    }


    fun updateNews() {
        val apiService = NewsApiService()
        job = GlobalScope.launch(Dispatchers.Main) {
            try {
                val newsResponse: NewsResponse = apiService.getPosts().await()

                val articles = RealmList<ArticleSave>()

                newsResponse.articles.forEach {
                    articles.add(
                        ArticleSave(
                            it.author, it.content, it.description, it.publishedAt,
                            title = it.title, url = it.url, urlToImage = it.urlToImage
                        )
                    )
                }

                val feed = articles//RealmList(NewsSave(articles, newsResponse.status, newsResponse.totalResults))

                Realm.getDefaultInstance().executeTransaction { realm ->
                    val oldList = realm.where(ArticleSave::class.java).findAll()
                    if (oldList.size > 0) {
                        for (item in oldList)
                            item.deleteFromRealm()
                    }
                    realm.copyToRealm(feed)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                Toast.makeText(context, "Нет подключения к сети", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun showRecView() {
        Realm.getDefaultInstance().executeTransaction { realm ->
            val feed = realm.where(ArticleSave::class.java).findAll()

            if (feed.size > 0) {
                recyclerView.adapter = RecyclerView(feed!!)
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recyclerView.itemAnimator = DefaultItemAnimator()
            }
        }
    }

    override fun onDetach() {
        job.cancelChildren()
        super.onDetach()
    }
}