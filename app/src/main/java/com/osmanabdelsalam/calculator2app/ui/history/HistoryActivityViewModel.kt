package com.osmanabdelsalam.calculator2app.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osmanabdelsalam.calculator2app.data.History
import com.osmanabdelsalam.calculator2app.repositories.HistoryRepository

class HistoryActivityViewModel(application: Application): AndroidViewModel(application) {
    private val _historyList: MutableLiveData<ArrayList<History>> by lazy {
        MutableLiveData<ArrayList<History>>()
    }
    val historyList: LiveData<ArrayList<History>> = _historyList

    private val historyRepository: HistoryRepository = HistoryRepository(application)

    init {
        _historyList.value = historyRepository.getHistoryList()
    }

    fun deleteItem(history: History) {
        if(historyRepository.deleteById(history.id)) {
            _historyList.value?.remove(history)
        }
    }

    fun clearItems() {
        historyRepository.clearHistory()
        _historyList.value?.clear()
    }
}