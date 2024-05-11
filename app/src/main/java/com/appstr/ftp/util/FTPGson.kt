package com.appstr.ftp.util

import com.appstr.ftp.data.jsonrequests.RedditJsonRequest
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object FTPGson {

    private val gson = GsonBuilder().setLenient().create()

    suspend fun parseRedditResponse(body: String): RedditJsonRequest {
        return gson.fromJson(body, RedditJsonRequest::class.java)
    }

}