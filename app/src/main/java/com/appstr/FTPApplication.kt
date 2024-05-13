package com.appstr

import android.app.Application
import com.appstr.ftp.network.ImageLoader

class FTPApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        ImageLoader.instantiate(this)
    }

}