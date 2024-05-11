package com.appstr.ftp.data

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName


data class ImageData(
    @SerializedName("url")
    @ColumnInfo(name = "imagedata_url")
    var url: String,

    @SerializedName("width")
    @ColumnInfo(name = "imagedata_width")
    var width: Int,

    @SerializedName("height")
    @ColumnInfo(name = "imagedata_height")
    var height: Int

)