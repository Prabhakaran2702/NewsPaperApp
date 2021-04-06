package com.prabhakaran.khaleejtimes_.ui.articledetails

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.prabhakaran.khaleejtimes_.R
import com.prabhakaran.khaleejtimes_.data.local.db.BookmarksDatabase
import com.prabhakaran.khaleejtimes_.data.repository.BookmarksRepository
import com.prabhakaran.khaleejtimes_.data.local.prefrences.AppPreferences
import com.prabhakaran.khaleejtimes_.databinding.FragmentArticlesBinding
import com.prabhakaran.khaleejtimes_.model.Article
import com.prabhakaran.khaleejtimes_.ui.adapter.NewsListAdapter
import com.prabhakaran.khaleejtimes_.ui.adapter.NewsTabAdapter
import com.prabhakaran.khaleejtimes_.ui.main.MainActivity
import com.prabhakaran.khaleejtimes_.ui.main.MainViewModel
import com.prabhakaran.khaleejtimes_.ui.main.MainViewModelFactory
import com.prabhakaran.khaleejtimes_.utils.HidingViewScrollListener
import com.prabhakaran.khaleejtimes_.utils.ObjectSerializer


class ArticlesFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    lateinit var binding: FragmentArticlesBinding
    private lateinit var newsListAdapter: NewsListAdapter
    private lateinit var newsTabAdapter: NewsTabAdapter
    private lateinit var preferences: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_articles, container, false)

        val database: BookmarksDatabase = BookmarksDatabase.getInstance(context = requireContext())
        val daoBook = database.daoBook
        val daoArticles = database.daoArticles
        val repository = BookmarksRepository(daoBook)

        preferences =
            AppPreferences(requireContext())

        val factory = MainViewModelFactory(daoArticles,repository, preferences)
        viewModel = ViewModelProvider(requireActivity(), factory).get(MainViewModel::class.java)


        binding.viewModel = viewModel


        defineViews()

        return binding.root
    }

    fun defineViews() {

        initAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.recyclerView.addOnScrollListener(object : HidingViewScrollListener() {
            override fun onHide() {
                MainActivity.toolbar.visibility = View.GONE
            }

            override fun onShow() {
                MainActivity.toolbar.visibility = View.VISIBLE
            }
        })


        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (!isLoading) {
                if (binding.swipeRefreshLayout.isRefreshing) binding.swipeRefreshLayout.isRefreshing =
                    false


                binding.recyclerView.visibility = View.VISIBLE


                val context = binding.recyclerView.context
                val layoutAnimationController =
                    AnimationUtils.loadLayoutAnimation(context, R.anim.layout_down_to_up)
                binding.recyclerView.layoutAnimation = layoutAnimationController
                displayArticles()

            } else {
                if (!binding.swipeRefreshLayout.isRefreshing) binding.swipeRefreshLayout.isRefreshing =
                    true
                binding.recyclerView.visibility = View.INVISIBLE
            }
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshResponse(false)
        }

    }


    private fun displayArticles() {

        viewModel.responseLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val articles = it.body()?.articles
                if (articles != null) {

                    if (binding.recyclerView.adapter is NewsTabAdapter)
                        newsTabAdapter.setArticleList(articles)
                    else
                        newsListAdapter.setArticleList(articles)

                }
            }
        })
    }

    private fun goToArticleDetailActivity(article: Article, imageView: ImageView) {
        val intent = Intent(requireContext(), ArticleDetailsActivity::class.java)
        intent.putExtra("article", ObjectSerializer.serialize(article))
        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            imageView,
            "article_image"
        )
        intent.putExtra("fab_visiblity", View.VISIBLE)
        startActivity(intent, activityOptions.toBundle())
    }


    private fun deleteBookmark(article: Article) {

        val bookmarks = viewModel.bookmarkList.value?.get(
            viewModel.responseLiveData.value?.body()!!.articles.indexOf(article)
        )
        if (bookmarks != null) {
            viewModel.deleteABookmark(bookmarks)
        }
    }


    private fun initAdapter(){

        newsListAdapter =   NewsListAdapter({ article: Article, imageView: ImageView ->
            goToArticleDetailActivity(article, imageView)
        }
            , {

            })

        newsTabAdapter =   NewsTabAdapter({ article: Article, imageView: ImageView ->
            goToArticleDetailActivity(article, imageView)
        }
            , { article ->

            })



        viewModel.appPrefrences.viewtype.asLiveData().observe(viewLifecycleOwner, Observer {viewType ->
//            if (viewType.equals("List"))
//                binding.recyclerView.adapter = newsListAdapter
//            else
//                binding.recyclerView.adapter = newsTabAdapter
            binding.recyclerView.adapter = newsListAdapter

        })

    }


}

