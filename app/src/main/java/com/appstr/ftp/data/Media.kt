package com.appstr.ftp.data

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName


data class Media(
    @SerializedName("type")
    @Embedded
    var type: String? = null
)