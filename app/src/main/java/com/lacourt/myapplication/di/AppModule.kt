package com.lacourt.myapplication.di

import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.network.Apifactory
import com.lacourt.myapplication.network.TmdbApi
import com.lacourt.myapplication.repository.SearchRepository
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        val authInterceptor = Interceptor { chain ->
            val newUrl = chain.request().url
                .newBuilder()
                .addQueryParameter("api_key", AppConstants.TMDB_API_KEY)
                .build()

            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }

        fun loggingClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .addNetworkInterceptor(authInterceptor)
                .build()
        }

        fun retrofit(): Retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(loggingClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit()
    }

    @Provides
    fun providesString(): String {
        return "Retornou!"
    }
//
//    @Provides
//    fun provideSearchRepository(tmdbApi: TmdbApi): SearchRepository{
//        return SearchRepository(tmdbApi)
//    }
}
