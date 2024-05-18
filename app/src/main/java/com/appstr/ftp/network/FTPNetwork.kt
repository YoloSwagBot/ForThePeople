package com.appstr.ftp.network

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText


@Module
@InstallIn(SingletonComponent::class)
object FTPNetwork {

    private val baseUrl_BadCopNoDonut = "https://www.reddit.com/r/Bad_Cop_No_Donut.json"

    private val httpClient = HttpClient()


    suspend fun requestBadCopNoDonut(): String {
        val response = httpClient.get(baseUrl_BadCopNoDonut)
        return response.bodyAsText()
    }

    suspend fun loadMore(after: String): String {
        val response = httpClient.get("$baseUrl_BadCopNoDonut?after=$after")
        return response.bodyAsText()
    }

}