package com.appstr.ftp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.appstr.ftp.network.FTPNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel(appli: Application): AndroidViewModel(appli) {

    private val _dataset = MutableStateFlow(arrayListOf<String>("a","b","c","d","e","f","g"))
    val dataset: StateFlow<ArrayList<String>>
        get() = _dataset.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()


    init {

    }


    fun refresh() {
        // Don't set _isRefreshing to true here as this will be called on init,
        //  the pull to refresh api will handle setting _isRefreshing to true
        viewModelScope.launch {
            // Doing the data refresh here
            async(Dispatchers.IO) {
                // Emitting the fetched data to the list
//                _item.emit(foxService.getRandomFox().await())
                val res = FTPNetwork.requestBadCopNoDonut()
                Log.d("CarsonDebug", res)
                _dataset.emit(arrayListOf(res))
            }.await()
            // Set _isRefreshing to false to indicate the refresh is complete
            _isRefreshing.emit(false)
        }
    }


}