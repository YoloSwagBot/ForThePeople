package com.appstr.ftp.data

import com.google.gson.annotations.SerializedName


data class Preview (
    @SerializedName("images")
//    @TypeConverters(ListImagesConverter::class)
    var images: List<Images>
){


}