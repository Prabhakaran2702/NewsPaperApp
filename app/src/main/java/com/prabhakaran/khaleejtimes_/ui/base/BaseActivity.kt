package com.prabhakaran.khaleejtimes_.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.prabhakaran.khaleejtimes_.data.local.prefrences.AppPreferences
import com.prabhakaran.khaleejtimes_.data.local.db.BookmarksDatabase
import com.prabhakaran.khaleejtimes_.data.repository.BookmarksRepository
import com.prabhakaran.khaleejtimes_.ui.main.MainViewModelFactory
import dev.somnath.onlynews.data.repo.ArticleRepository

abstract class BaseActivity<binding : ViewDataBinding, viewModel : ViewModel, viewModelstoreOwner : ViewModelStoreOwner>() :
    AppCompatActivity() {

    abstract fun getViewBinding(): binding
    abstract fun getViewModel(): Class<viewModel>
    abstract fun getViewModelStoreOwner(): viewModelstoreOwner
    abstract fun getContext(): Context

    protected lateinit var binding: binding

    protected lateinit var viewModel: viewModel

    protected lateinit var prefrences: AppPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        val database: BookmarksDatabase =
            BookmarksDatabase.getInstance(context = applicationContext)
        val daoBook = database.daoBook
        val daoArticles = database.daoArticles

        val bookmarksRepository = BookmarksRepository(daoBook)

        prefrences = AppPreferences(this)

        val factory = MainViewModelFactory(daoArticles,bookmarksRepository, prefrences)
        viewModel = ViewModelProvider(getViewModelStoreOwner(), factory).get(getViewModel())
    }
}