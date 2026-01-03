package com.appstudio.gestordelecturapersonal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.appstudio.gestordelecturapersonal.data.local.entity.PendingDeleteEntity

@Dao
interface PendingDeleteDao {

    @Insert
    suspend fun insert(pendingDelete: PendingDeleteEntity)

    @Query("SELECT * FROM pending_deletes")
    suspend fun getAll(): List<PendingDeleteEntity>

    @Query("DELETE FROM pending_deletes WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM pending_deletes")
    suspend fun clearAll()
}