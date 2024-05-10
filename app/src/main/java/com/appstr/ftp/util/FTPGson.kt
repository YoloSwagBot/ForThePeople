package com.appstr.ftp.util

import com.appstr.ftp.data.RedditResponse
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object FTPGson {

    private val gson = GsonBuilder().setLenient().create()

    suspend fun parseRedditResponse(body: String): RedditResponse {
        return gson.fromJson(body, RedditResponse::class.java)
    }

}