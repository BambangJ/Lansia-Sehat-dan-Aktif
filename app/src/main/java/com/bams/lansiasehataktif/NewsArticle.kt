package com.bams.lansiasehataktif

data class NewsArticle(
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val category: String
)

data class NewsResponse(
    val articles: List<NewsArticle>
)
