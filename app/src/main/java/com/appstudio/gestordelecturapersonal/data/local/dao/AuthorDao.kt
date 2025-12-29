package com.appstudio.gestordelecturapersonal.data.local.dao

import androidx.room.*
import com.appstudio.gestordelecturapersonal.data.local.entity.AuthorEntity
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
}
