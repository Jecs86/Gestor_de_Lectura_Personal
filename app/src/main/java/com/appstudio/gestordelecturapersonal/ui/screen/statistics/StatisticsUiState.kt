package com.appstudio.gestordelecturapersonal.ui.screen.statistics

data class StatisticsUiState(
    val totalBooks: Int = 0,
    val readBooks: Int = 0,
    val pendingBooks: Int = 0,
    val pagesRead: Int = 0,
    val progressPercent: Float = 0f,
    val isLoading: Boolean = true
)
