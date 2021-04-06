package com.prabhakaran.khaleejtimes_.model

import com.prabhakaran.khaleejtimes_.model.Article

data class NewsItem(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)