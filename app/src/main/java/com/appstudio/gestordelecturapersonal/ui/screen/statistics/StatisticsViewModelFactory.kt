package com.appstudio.gestordelecturapersonal.ui.screen.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appstudio.gestordelecturapersonal.data.local.dao.BookDao

class StatisticsViewModelFactory (
private val bookDao: BookDao,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            return StatisticsViewModel(
                bookDao
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}