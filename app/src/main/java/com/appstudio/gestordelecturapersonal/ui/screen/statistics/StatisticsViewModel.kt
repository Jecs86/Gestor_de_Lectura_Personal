package com.appstudio.gestordelecturapersonal.ui.screen.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstudio.gestordelecturapersonal.data.local.dao.BookDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val bookDao: BookDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState

    private val uid: String =
        FirebaseAuth.getInstance().currentUser!!.uid

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {

            val total = bookDao.countTotalBooks(uid)
            val read = bookDao.countReadBooks(uid)
            val pending = bookDao.countPendingBooks(uid)
            val pages = bookDao.sumPagesRead(uid) ?: 0

            val progress =
                if (total == 0) 0f
                else (read.toFloat() / total) * 100f

            _uiState.value = StatisticsUiState(
                totalBooks = total,
                readBooks = read,
                pendingBooks = pending,
                pagesRead = pages,
                progressPercent = progress,
                isLoading = false
            )
        }
    }
}