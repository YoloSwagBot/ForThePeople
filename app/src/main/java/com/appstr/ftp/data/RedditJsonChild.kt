package com.appstr.ftp.data

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class RedditJsonChild(

        @SerializedName("data")
        @Embedded
        var data: RedditJsonChildData?

)
