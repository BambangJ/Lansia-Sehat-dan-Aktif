package com.bams.lansiasehataktif

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/everything")
    fun getNews(
        @Query("q") query: String = "Lansia OR Dinas Kesehatan",
        @Query("apiKey") apiKey: String,
        @Query("language") language: String = "id"
    ): Call<NewsResponse>
}

