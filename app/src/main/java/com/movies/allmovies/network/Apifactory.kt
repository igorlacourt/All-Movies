package com.movies.allmovies.network
//
//import com.movies.allmovies.BuildConfig
//import com.movies.allmovies.AppConstants
//import okhttp3.*
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
//import retrofit2.converter.gson.GsonConverterFactory
//import java.util.concurrent.TimeUnit
//
//object Apifactory {
//    //Creating Auth Interceptor to add api_key query in front of all the requests.
//    private val authInterceptor = Interceptor { chain ->
//        val newUrl = chain.request().url
//            .newBuilder()
//            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
//            .build()
//
//        val newRequest = chain.request()
//            .newBuilder()
//            .url(newUrl)
//            .build()
//
//        chain.proceed(newRequest)
//    }
//
//    private fun loggingClient(): OkHttpClient{
//        val interceptor = HttpLoggingInterceptor()
//        interceptor.level = HttpLoggingInterceptor.Level.BODY
//
//        return OkHttpClient.Builder()
//            .readTimeout(15, TimeUnit.SECONDS)
//            .connectTimeout(15, TimeUnit.SECONDS)
//            .addNetworkInterceptor(interceptor)
//            .addNetworkInterceptor(authInterceptor)
//            .build()
//    }
//
//    private fun retrofit(): Retrofit = Retrofit.Builder()
//        .baseUrl(AppConstants.TMDB_BASE_URL)
//        .client(loggingClient())
//        .addConverterFactory(GsonConverterFactory.create())
//        .addCallAdapterFactory(NetworkResponseAdapterFactory())
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .build()
//
//    val tmdbApi: TmdbApi = retrofit().create(TmdbApi::class.java)
//}
//
