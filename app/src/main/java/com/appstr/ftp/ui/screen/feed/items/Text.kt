package com.appstr.ftp.ui.screen.feed.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appstr.ftp.data.RedditJsonChild
import com.appstr.ftp.ui.screen.feed.titleAreaHeight


@Composable
fun FeedItemText(
    data: RedditJsonChild
){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Title
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(titleAreaHeight)
                .padding(4.dp),
            text = data.data?.title ?: "N/A",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        // Desc/etc
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            text = data.data?.sefttext ?: "error reading text",
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis
        )
    }
}

