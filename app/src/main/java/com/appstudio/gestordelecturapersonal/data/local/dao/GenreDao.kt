package com.appstudio.gestordelecturapersonal.data.local.dao

import androidx.room.*
import com.appstudio.gestordelecturapersonal.data.local.entity.BookEntity
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

    @Query("SELECT * FROM genres")
    suspend fun obtenerGenerosOnce(): List<GenreEntity>

    @Query("""
    UPDATE genres
    SET syncPending = 1,
        fechaActualizacion = :timestamp
    WHERE id = :genreId
    """)
    suspend fun markSyncPending(
        genreId: Long,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("SELECT * FROM genres WHERE syncPending = 1")
    suspend fun getPendingSyncGenres(): List<GenreEntity>

    @Query("UPDATE genres SET syncPending = 0 WHERE id = :genreId")
    suspend fun clearSyncPending(genreId: Long)
}
