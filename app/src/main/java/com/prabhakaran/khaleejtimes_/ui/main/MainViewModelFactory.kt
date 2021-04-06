package com.prabhakaran.khaleejtimes_.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prabhakaran.khaleejtimes_.data.local.db.ArticleCollectionDAO
import com.prabhakaran.khaleejtimes_.data.local.prefrences.AppPreferences
import com.prabhakaran.khaleejtimes_.data.repository.BookmarksRepository
import java.lang.IllegalArgumentException

class MainViewModelFactory(private val doa: ArticleCollectionDAO, private val bookmarksRepository: BookmarksRepository, private val preferences: AppPreferences): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(doa,bookmarksRepository,preferences) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}