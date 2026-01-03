package com.appstudio.gestordelecturapersonal.data.local.dao

import androidx.room.*
import com.appstudio.gestordelecturapersonal.data.local.entity.BookEntity
import com.appstudio.gestordelecturapersonal.ui.screen.books.list.BookUiModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("""
        SELECT * FROM books
        WHERE uid = :uid AND estaEliminado = 0
        ORDER BY fechaActualizacion DESC
    """)
    fun obtenerLibros(uid: String): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(book: BookEntity)

    @Update
    suspend fun actualizar(book: BookEntity)

    @Query("""
        UPDATE books
        SET estaEliminado = 1, fechaActualizacion = :timestamp
        WHERE id = :id
    """)
    suspend fun eliminarLogico(id: Long, timestamp: Long)

    @Query("SELECT * FROM books WHERE id = :id LIMIT 1")
    suspend fun obtenerPorUid(id: Long): BookEntity?

    @Query("""
        SELECT 
            b.id,
            b.titulo,
            a.nombre AS autor,
            g.nombre AS genero,
            b.paginasLeidas,
            b.paginasTotales,
            b.estado,
            b.urlPortada
        FROM books b
        INNER JOIN authors a ON b.authorId = a.id
        INNER JOIN genres g ON b.genreId = g.id
        WHERE b.estaEliminado = 0
        ORDER BY b.fechaCreacion DESC
    """)
    fun obtenerLibrosUi(): Flow<List<BookUiModel>>

    @Query("""
        SELECT *
        FROM books b
        WHERE b.estaEliminado = 1
        ORDER BY b.fechaActualizacion DESC
    """)
    fun obtenerLibrosEliminados(): Flow<List<BookEntity>>

    @Query("""
        UPDATE books
        SET estaEliminado = 0,
            fechaActualizacion = :timestamp
        WHERE id = :id
    """)
    suspend fun restaurarLibro(
        id: Long,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun eliminarLibroDefinitivo(id: Long)

    @Query("SELECT * FROM books WHERE uid = :uid")
    suspend fun obtenerLibrosOnce(uid: String): List<BookEntity>

    @Query("""
    UPDATE books
    SET syncPending = 1,
        fechaActualizacion = :timestamp
    WHERE id = :bookId
    """)
    suspend fun markSyncPending(
        bookId: Long,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("SELECT * FROM books WHERE syncPending = 1")
    suspend fun getPendingSyncBooks(): List<BookEntity>

    @Query("UPDATE books SET syncPending = 0 WHERE id = :bookId")
    suspend fun clearSyncPending(bookId: Long)

    // statistics

    @Query("""
    SELECT COUNT(*) 
    FROM books 
    WHERE uid = :uid AND estaEliminado = 0
""")
    suspend fun countTotalBooks(uid: String): Int

    @Query("""
    SELECT COUNT(*) 
    FROM books 
    WHERE uid = :uid 
      AND estado = 'COMPLETADO'
      AND estaEliminado = 0
    """)
    suspend fun countReadBooks(uid: String): Int

    @Query("""
    SELECT COUNT(*) 
    FROM books 
    WHERE uid = :uid 
      AND estado != 'COMPLETADO'
      AND estaEliminado = 0
    """)
    suspend fun countPendingBooks(uid: String): Int

    @Query("""
    SELECT 
        SUM(paginasLeidas) 
    FROM books 
    WHERE uid = :uid AND estaEliminado = 0
    """)
    suspend fun sumPagesRead(uid: String): Int?

}