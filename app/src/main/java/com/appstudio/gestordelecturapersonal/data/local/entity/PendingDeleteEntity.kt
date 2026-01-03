package com.appstudio.gestordelecturapersonal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_deletes")
data class PendingDeleteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val entityType: String, // "book", "note", "author", "genre"
    val entityId: Long,
    val parentId: Long? = null,
    val timestamp: Long = System.currentTimeMillis()
)