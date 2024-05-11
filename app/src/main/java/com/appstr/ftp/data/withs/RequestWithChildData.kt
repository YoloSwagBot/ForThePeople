package com.appstr.ftp.data.withs

import androidx.room.Embedded
import androidx.room.Relation
import com.appstr.ftp.data.RedditJsonChildData
import com.appstr.ftp.data.jsonrequests.RedditJsonRequest


class RequestWithChildData(

    @Embedded val request: RedditJsonRequest,

    @Relation(
        parentColumn = "requestId",
        entity = RedditJsonChildData::class,
        entityColumn = "childDataId"
    )
//    @TypeConverters(ListRedditJsonChildConverter::class)
    val childData: List<RedditJsonChildData>

)
