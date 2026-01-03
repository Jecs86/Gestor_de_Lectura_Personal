package com.appstudio.gestordelecturapersonal.data.local.dao

import androidx.room.*
import com.appstudio.gestordelecturapersonal.data.local.entity.BookEntity
import com.appstudio.gestordelecturapersonal.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("""
        SELECT * FROM notes
        WHERE bookId = :bookId AND estaEliminado = 0
        ORDER BY fechaActualizacion DESC
    """)
    fun obtenerNotas(bookId: Long): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :noteId LIMIT 1")
    suspend fun obtenerPorId(noteId: Long): NoteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(note: NoteEntity)

    @Update
    suspend fun actualizar(note: NoteEntity)

    @Query("""
        UPDATE notes
        SET estaEliminado = 1,
            fechaActualizacion = :fecha
        WHERE id = :noteId
    """)
    suspend fun softDeleteNote(
        noteId: Long,
        fecha: Long = System.currentTimeMillis()
    )

    @Query("""
        SELECT * FROM notes
        WHERE bookId = :bookId
        AND estaEliminado = 1
        ORDER BY fechaActualizacion DESC
    """)
    fun getDeletedNotesByBook(bookId: Long): Flow<List<NoteEntity>>

    @Query("""
        UPDATE notes
        SET estaEliminado = 0,
            fechaActualizacion = :timestamp
        WHERE id = :noteId
    """)
    suspend fun restoreNote(
        noteId: Long,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        DELETE FROM notes
        WHERE id = :noteId
    """)
    suspend fun deleteNoteForever(noteId: Long)

    @Query("DELETE FROM notes WHERE bookId = :bookId")
    suspend fun deleteNotesByBook(bookId: Long)

    @Query("SELECT * FROM notes WHERE bookId = :bookId")
    suspend fun obtenerNotasPorLibroOnce(bookId: Long): List<NoteEntity>

    @Query("""
    UPDATE notes
    SET syncPending = 1,
        fechaActualizacion = :timestamp
    WHERE id = :noteId
    """)
    suspend fun markSyncPending(
        noteId: Long,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("SELECT * FROM notes WHERE syncPending = 1")
    suspend fun getPendingSyncNotes(): List<NoteEntity>

    @Query("UPDATE notes SET syncPending = 0 WHERE id = :noteId")
    suspend fun clearSyncPending(noteId: Long)
}
