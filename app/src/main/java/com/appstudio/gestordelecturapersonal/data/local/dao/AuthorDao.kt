package com.appstudio.gestordelecturapersonal.data.local.dao

import androidx.room.*
import com.appstudio.gestordelecturapersonal.data.local.entity.AuthorEntity
import com.appstudio.gestordelecturapersonal.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthorDao {

    @Query("""
        SELECT * FROM authors
        WHERE estaEliminado = 0
        ORDER BY nombre
    """)
    fun obtenerAutores(): Flow<List<AuthorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(author: AuthorEntity)

    @Query("SELECT * FROM authors")
    suspend fun obtenerAutoresOnce(): List<AuthorEntity>

    @Query("""
    UPDATE authors
    SET syncPending = 1,
        fechaActualizacion = :timestamp
    WHERE id = :authorId
    """)
    suspend fun markSyncPending(
        authorId: Long,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("SELECT * FROM authors WHERE syncPending = 1")
    suspend fun getPendingSyncAuthors(): List<AuthorEntity>

    @Query("UPDATE authors SET syncPending = 0 WHERE id = :authorId")
    suspend fun clearSyncPending(authorId: Long)
}
