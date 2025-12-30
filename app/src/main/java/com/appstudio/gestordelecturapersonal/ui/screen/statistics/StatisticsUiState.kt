package com.appstudio.gestordelecturapersonal.ui.screen.statistics

data class StatisticsUiState(
    val totalLibros: Int,
    val librosLeidos: Int,
    val librosEnProgreso: Int,
    val paginasLeidas: Int
) {
    companion object {
        fun fake() = StatisticsUiState(
            totalLibros = 12,
            librosLeidos = 5,
            librosEnProgreso = 4,
            paginasLeidas = 2340
        )
    }
}
