package com.appstr.ftp.network

import android.app.Application
import coil.ImageLoader
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object ImageLoader {

    private lateinit var imageLoader: ImageLoader

    fun instantiate(application: Application){

    }

    fun load(){

    }

}