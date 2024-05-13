package com.appstr.ftp.network

import com.appstr.ftp.util.baseUrl_reddit
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText


@Module
@InstallIn(SingletonComponent::class)
object FTPNetwork {

    private val baseUrl_BadCopNoDonut = baseUrl_reddit

    private val httpClient = HttpClient()


    suspend fun requestBadCopNoDonut(): String {
        val response = httpClient.get(baseUrl_BadCopNoDonut)
        return response.bodyAsText()
    }

    suspend fun loadMore(): String {
        return ""
    }

}