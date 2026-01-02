package com.appstudio.gestordelecturapersonal.data.local.dao

import androidx.room.*
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
}
