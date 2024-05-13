package com.appstr.ftp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.appstr.ftp.data.RedditJsonChild
import com.appstr.ftp.network.FTPNetwork
import com.appstr.ftp.ui.screen.content.Screen
import com.appstr.ftp.util.FTPGson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.ArrayDeque


class MainVM(appli: Application): AndroidViewModel(appli) {

    private val _screenStack = MutableStateFlow(ArrayDeque<Screen>(listOf(Screen.MainScreen())))
    val screenStack: StateFlow<ArrayDeque<Screen>>
        get() = _screenStack.asStateFlow()

    private val _dataset = MutableStateFlow(listOf<RedditJsonChild>())
    val dataset: StateFlow<List<RedditJsonChild>>
        get() = _dataset.asStateFlow()

    private val _isRefreshing = MutableStateFlow(true)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(true)
    val isLoadingMore: StateFlow<Boolean>
        get() = _isLoadingMore.asStateFlow()


    init {
        refresh()
    }


    fun refresh() {
        // Don't set _isRefreshing to true here as this will be called on init,
        //  the pull to refresh api will handle setting _isRefreshing to true
        viewModelScope.launch {
            _isRefreshing.emit(true)
            // Doing the data refresh here
            async(Dispatchers.IO) {
                _dataset.emit(arrayListOf())
                // Emitting the fetched data to the list
                val res = FTPNetwork.requestBadCopNoDonut()
//                Log.d("CarsonDebug", res)
                val arr = FTPGson.parseRedditResponse(res)
//                Log.d("CarsonDebug", "arr: $arr")
                val newDataset = arrayListOf<RedditJsonChild>()
                arr.data?.children?.let { newDataset.addAll(it) }
                _dataset.emit(newDataset.filter { it.data?.stickied == false })
            }.await()
            // Set _isRefreshing to false to indicate the refresh is complete
            _isRefreshing.emit(false)
        }
    }

    fun loadMore(){

    }

    fun addScreen(screen: Screen){
//        Log.d("Carson", "MainVM - addScreen($screen)")
        viewModelScope.launch {
            val newStack = _screenStack.value.clone()
            newStack.add(screen)
            _screenStack.emit(newStack)
        }
    }

    fun removeScreen(){
        viewModelScope.launch {
            val newStack = _screenStack.value.clone()
            if (newStack.size == 1) return@launch
            newStack.removeLast()
            _screenStack.emit(newStack)
        }
    }

}