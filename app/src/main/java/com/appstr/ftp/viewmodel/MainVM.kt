package com.appstr.ftp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.appstr.ftp.data.RedditJsonChild
import com.appstr.ftp.network.FTPNetwork
import com.appstr.ftp.util.FTPGson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainVM(appli: Application): AndroidViewModel(appli) {

    private val _dataset = MutableStateFlow(listOf<RedditJsonChild>())
    val dataset: StateFlow<List<RedditJsonChild>>
        get() = _dataset.asStateFlow()

    private val _isRefreshing = MutableStateFlow(true)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()


    init {
        refresh()
    }


    fun refresh() {
        Log.d("CarsonDebug", "MainVM -- refresh()")
        // Don't set _isRefreshing to true here as this will be called on init,
        //  the pull to refresh api will handle setting _isRefreshing to true
        viewModelScope.launch {
            _isRefreshing.emit(true)
            // Doing the data refresh here
            async(Dispatchers.IO) {
                // Emitting the fetched data to the list
//                _item.emit(foxService.getRandomFox().await())
                val res = FTPNetwork.requestBadCopNoDonut()
//                Log.d("CarsonDebug", res)
                val arr = FTPGson.parseRedditResponse(res)
//                Log.d("CarsonDebug", "arr: $arr")
                arr.data?.children?.let { _dataset.emit(it) }
            }.await()
            // Set _isRefreshing to false to indicate the refresh is complete
            _isRefreshing.emit(false)
        }
    }


}