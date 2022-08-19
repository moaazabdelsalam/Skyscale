package com.io.skyscale.api

import com.io.skyscale.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

fun createSkyscaleService() : SkyscaleService {

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    return retrofit.create(SkyscaleService::class.java)
}

interface SkyscaleService {

    @GET("/")
    fun serverResult(
        @Query("image") imageBytes: String,
        @Query("scale") scale: String,
        @Query("feature") feature: String,
        @Query("token") token: String
    ): Call<ResultImage>

    @POST("/")
    fun upscalePostRequest(
        @Body post: UpscalePostRequest
    ): Call<ResultImage>

    @POST("/")
    fun otherFeaturesPostRequest(
        @Body post: OtherFeaturesPostRequest
    ): Call<ResultImage>
}