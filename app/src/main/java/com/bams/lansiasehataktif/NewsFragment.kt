package com.bams.lansiasehataktif

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsFragment<T> : Fragment() {

    private lateinit var recyclerViewNews: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var graph: GraphView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and GraphView
        recyclerViewNews = view.findViewById(R.id.recyclerViewNews)
        recyclerViewNews.layoutManager = LinearLayoutManager(context)

        graph = view.findViewById(R.id.graph)

        // Setup Firebase Remote Config
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600) // 1 hour
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Fetch remote config and load data
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("NewsFragment", "Remote Config fetch succeeded")
                    fetchNews()
                    fetchLansiaData()
                } else {
                    Log.e("NewsFragment", "Remote Config fetch failed")
                }
            }
    }

    private fun fetchNews() {
        val apiKey = remoteConfig.getString("news_api_key")
        val language = "id" // Filter news by Indonesian language

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(NewsApiService::class.java)
        service.getNews("Kesehatan lansia OR Kesehatan", apiKey, language).enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val newsArticles = response.body()?.articles
                    if (newsArticles != null) {
                        newsAdapter = NewsAdapter(newsArticles)
                        recyclerViewNews.adapter = newsAdapter
                    }
                } else {
                    Log.e("NewsFragment", "Failed to fetch news: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("NewsFragment", "Failed to fetch news", t)
            }
        })
    }

    private fun fetchLansiaData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://data.jabarprov.go.id/api-backend/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getLansiaData().enqueue(object : Callback<List<LansiaData>> {
            override fun onResponse(call: Call<List<LansiaData>>, response: Response<List<LansiaData>>) {
                if (response.isSuccessful) {
                    val lansiaData = response.body()

                    // Convert the data to DataPoint array
                    val dataPoints = lansiaData?.mapIndexed { index, data ->
                        DataPoint(index.toDouble(), data.jumlah_lansia.toDouble())
                    }?.toTypedArray()

                    // Update the GraphView with the data
                    val series = BarGraphSeries(dataPoints)
                    graph.addSeries(series)

                    // Customize the graph
                    series.spacing = 10
                    series.isDrawValuesOnTop = true
                    series.valuesOnTopColor = Color.RED

                    graph.title = "Jumlah Lansia 2019"
                    graph.gridLabelRenderer.horizontalAxisTitle = "Desa/Kelurahan"
                    graph.gridLabelRenderer.verticalAxisTitle = "Jumlah Lansia"
                }
            }

            override fun onFailure(call: Call<List<LansiaData>>, t: Throwable) {
                Log.e("NewsFragment", "Failed to fetch lansia data", t)
            }
        })
    }
}
