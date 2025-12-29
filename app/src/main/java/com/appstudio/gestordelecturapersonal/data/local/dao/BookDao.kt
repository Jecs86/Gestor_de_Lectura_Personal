package com.appstudio.gestordelecturapersonal.data.local.dao

import androidx.room.*
import com.appstudio.gestordelecturapersonal.data.local.entity.BookEntity
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

    @Query("UPDATE books SET estaEliminado = 1 WHERE id = :id")
    suspend fun eliminarLogico(id: Long)
}