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
}