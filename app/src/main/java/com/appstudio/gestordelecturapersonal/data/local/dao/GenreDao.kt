package com.appstudio.gestordelecturapersonal.data.local.dao

import androidx.room.*
import com.appstudio.gestordelecturapersonal.data.local.entity.GenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Query("""
        SELECT * FROM genres
        WHERE estaEliminado = 0
        ORDER BY nombre
    """)
    fun obtenerGeneros(): Flow<List<GenreEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(genre: GenreEntity)
}
