package fr.badetitou.bibliositpi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.biblio.sitpi.fr/"


    val retrofit: Retrofit by lazy {

//        val interceptor = HttpLoggingInterceptor()
//        interceptor.level = HttpLoggingInterceptor.Level.BODY
//        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
//            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object SitpiClient {
    val apiService: SitpiService by lazy {
        RetrofitClient.retrofit.create(SitpiService::class.java)
    }
}