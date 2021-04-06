package dev.somnath.onlynews.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.prabhakaran.khaleejtimes_.data.local.db.ArticleCollectionDAO
import com.prabhakaran.khaleejtimes_.data.local.db.BookmarkDAO
import com.prabhakaran.khaleejtimes_.model.NewsItem
import com.prabhakaran.khaleejtimes_.data.remote.NewsService
import com.prabhakaran.khaleejtimes_.data.remote.RetrofitInstance
import retrofit2.Response

class ArticleRepository(){
    private val retService = RetrofitInstance.getRetrofitInstance().create(
        NewsService::class.java)

    private val responseLiveDataLiveData: LiveData<Response<NewsItem>> = liveData {
        val response = retService.getNews()
        emit(response)
    }

    lateinit   var responseCategoryLiveData: MutableLiveData<Response<NewsItem>>
    init {
       responseCategoryLiveData = responseLiveDataLiveData as MutableLiveData<Response<NewsItem>>

    }

    suspend fun getNews(): LiveData<Response<NewsItem>> = responseLiveDataLiveData

    suspend fun getNews(category: String):Response<NewsItem> {

        return when(category){

            "Technology" ->  retService.getNewsOnTech()

            "General" ->  retService.getNewsOnGeneral()

            "Business" ->  retService.getNewsOnBusiness()

            "Top Headlines" ->  retService.getNews()

            "Health" -> retService.getNewsOnHealth()

            "Entertainment" -> retService.getNewsOnEntertainment()

            "Sports" -> retService.getNewsOnSports()

            "Science" -> retService.getNewsOnScience()

            else ->  retService.getNews()
        }


    }
}