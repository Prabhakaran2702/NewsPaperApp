package com.prabhakaran.khaleejtimes_.ui.main

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prabhakaran.khaleejtimes_.R
import com.prabhakaran.khaleejtimes_.data.local.db.ArticleCollectionDAO
import com.prabhakaran.khaleejtimes_.data.local.prefrences.AppPreferences
import com.prabhakaran.khaleejtimes_.model.Bookmarks
import com.prabhakaran.khaleejtimes_.data.repository.BookmarksRepository
import com.prabhakaran.khaleejtimes_.model.NewsItem
import dev.somnath.onlynews.data.repo.ArticleRepository
import com.prabhakaran.khaleejtimes_.model.Article
import com.prabhakaran.khaleejtimes_.model.ArticleCollections
import com.prabhakaran.khaleejtimes_.utils.Event
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class MainViewModel(private val doa: ArticleCollectionDAO,
                    private val bookmarksRepository: BookmarksRepository,
                    private val prefrences: AppPreferences
) : ViewModel(), Observable {
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    @Bindable
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    val category: MutableLiveData<String> = MutableLiveData("Top Headlines")
    private val message : MutableLiveData<Event<String>> = MutableLiveData()

    val errorMessage
    get() = message

    var responseLiveData: MutableLiveData<Response<NewsItem>> = MutableLiveData()




    var bookmarkList: LiveData<List<Bookmarks>> = bookmarksRepository.bookmarks

    val appPrefrences: AppPreferences
    get() = prefrences




    fun refreshResponse(isOffline: Boolean) {

        viewModelScope.launch {
            isLoading.value = true
            try {

                if(isOffline){

                    var local: LiveData<List<ArticleCollections>> =  doa.getAllArticleCollections(category.value ?: "")
                  var data =local

                }

                else{



                    val response = ArticleRepository().getNews(category.value!!)
                    if (response.isSuccessful) {
                        responseLiveData.postValue(response)

                        var articles = response.body()?.articles


                        if(articles!=null){

                            var  articleCollections:ArrayList<ArticleCollections> = ArrayList<ArticleCollections>()


                            for (item: Article in articles) {
                                articleCollections.add(ArticleCollections(0,category.value ?: "", item))
                            }

                            doa.insertArticleCollections(articleCollections)

                        }



                    }
                    else {
                        responseLiveData.postValue(null)
                        message.value = Event(response.errorBody().toString())
                    }


                }


            }catch (e: Exception){
                responseLiveData.postValue(null)
                message.value = Event(e.toString())
            }

            isLoading.value = false
        }

    }

    fun changeViewType(id: Int) {

        when (id) {
            R.id.radio_button1 -> {
                viewModelScope.launch {
                    prefrences.saveViewType("List")
                }
            }
            R.id.radio_button2 -> {
                viewModelScope.launch {
                    prefrences.saveViewType("Tab")
                }
            }
        }

        refreshResponse(false)
    }

    fun addABookmark(id: Int = 0, article: Article) {

        viewModelScope.launch {
            val bookMark = Bookmarks(id, article)
            bookmarksRepository.insertArticleIntoBookmarks(bookMark)
        }

    }

    fun deleteABookmark(bookmark: Bookmarks) {
        viewModelScope.launch {
            bookmarksRepository.deleteArticlefromBookmarks(bookmark)
        }
    }

    fun clearAllBookmarks() {
        viewModelScope.launch {
            bookmarksRepository.deleteALlArticleFromBookmarks()
        }
    }

}