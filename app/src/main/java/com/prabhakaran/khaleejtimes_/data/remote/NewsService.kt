package com.prabhakaran.khaleejtimes_.data.remote

import com.prabhakaran.khaleejtimes_.BuildConfig
import com.prabhakaran.khaleejtimes_.model.NewsItem
import retrofit2.Response
import retrofit2.http.GET

interface NewsService{

    @GET("/v2/top-headlines?country=us&apiKey=${BuildConfig.NewsApiKey}")
    suspend fun getNews(): Response<NewsItem>
    @GET("/v2/top-headlines?country=us&category=business&apiKey=${BuildConfig.NewsApiKey}")
    suspend fun getNewsOnBusiness(): Response<NewsItem>
    @GET("/v2/top-headlines?country=us&category=general&apiKey=${BuildConfig.NewsApiKey}")
    suspend fun getNewsOnGeneral(): Response<NewsItem>
    @GET("/v2/top-headlines?country=us&category=health&apiKey=${BuildConfig.NewsApiKey}")
    suspend fun getNewsOnHealth(): Response<NewsItem>
    @GET("/v2/top-headlines?country=us&category=technology&apiKey=${BuildConfig.NewsApiKey}")
    suspend fun getNewsOnTech(): Response<NewsItem>
    @GET("/v2/top-headlines?country=us&category=science&apiKey=${BuildConfig.NewsApiKey}")
    suspend fun getNewsOnScience(): Response<NewsItem>
    @GET("/v2/top-headlines?country=us&category=entertainment&apiKey=${BuildConfig.NewsApiKey}")
    suspend fun getNewsOnEntertainment(): Response<NewsItem>
    @GET("/v2/top-headlines?country=us&category=sports&apiKey=${BuildConfig.NewsApiKey}")
    suspend fun getNewsOnSports(): Response<NewsItem>

}