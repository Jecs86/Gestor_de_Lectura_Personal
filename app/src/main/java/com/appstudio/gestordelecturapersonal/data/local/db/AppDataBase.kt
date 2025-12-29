package com.appstudio.gestordelecturapersonal.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.appstudio.gestordelecturapersonal.data.local.dao.AuthorDao
import com.appstudio.gestordelecturapersonal.data.local.dao.BookDao
import com.appstudio.gestordelecturapersonal.data.local.dao.GenreDao
import com.appstudio.gestordelecturapersonal.data.local.dao.NoteDao
import com.appstudio.gestordelecturapersonal.data.local.entity.AuthorEntity
import com.appstudio.gestordelecturapersonal.data.local.entity.BookEntity
import com.appstudio.gestordelecturapersonal.data.local.entity.GenreEntity
import com.appstudio.gestordelecturapersonal.data.local.entity.NoteEntity

@Database(
    entities = [
        BookEntity::class,
        AuthorEntity::class,
        GenreEntity::class,
        NoteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun authorDao(): AuthorDao
    abstract fun genreDao(): GenreDao
    abstract fun noteDao(): NoteDao
}