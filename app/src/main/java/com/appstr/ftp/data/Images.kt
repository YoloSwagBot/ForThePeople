package com.appstr.ftp.data

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName


data class Images(
    @SerializedName("source")
    @Embedded
    var source: ImageData? = null,

    @SerializedName("resolutions")
//    @TypeConverters(ListSourceConverter::class)
    var resolutions: List<ImageData> = listOf(),

    @SerializedName("id")
    var imageId: String

){

}
