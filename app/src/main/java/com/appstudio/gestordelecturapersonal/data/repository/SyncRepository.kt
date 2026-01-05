package com.appstudio.gestordelecturapersonal.data.repository

import android.util.Log
import com.appstudio.gestordelecturapersonal.data.local.db.AppDatabase
import com.appstudio.gestordelecturapersonal.data.local.entity.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SyncRepository(
    private val db: AppDatabase,
    private val firestore: FirebaseFirestore
) {

    /* -------------------------------
       🔽 FIRESTORE → ROOM
       ------------------------------- */

    suspend fun syncFromFirestore(uid: String) {
        // Limpia Room primero
        Log.d("SYNC", "Iniciando sincronización (firebase - room) para $uid")

        withContext(Dispatchers.IO) {
            try {
                db.clearAllTables()

                val userRef = firestore.collection("users").document(uid)

                // Authors
                userRef.collection("authors").get().await().documents.forEach {
                    it.toObject(AuthorEntity::class.java)?.let { author ->
                        db.authorDao().insertar(author)
                    }
                }

                // Genres
                userRef.collection("genres").get().await().documents.forEach {
                    it.toObject(GenreEntity::class.java)?.let { genre ->
                        db.genreDao().insertar(genre)
                    }
                }

                // Books + Notes
                userRef.collection("books").get().await().documents.forEach { bookDoc ->
                    val book = bookDoc.toObject(BookEntity::class.java) ?: return@forEach
                    db.bookDao().insertar(book)

                    bookDoc.reference.collection("notes")
                        .get().await().documents.forEach { noteDoc ->
                            noteDoc.toObject(NoteEntity::class.java)?.let { note ->
                                db.noteDao().insertar(note)
                            }
                        }
                }
            }catch (e: Exception) {
                Log.e("SYNC", "Error al sincronizar: ${e.message}")
                e.printStackTrace()
            }

        }
        Log.d("SYNC", "terminando sincronización (firebase - room) para $uid")
    }

    /* -------------------------------
       🔼 ROOM → FIRESTORE
       ------------------------------- */

    suspend fun syncToFirestore(uid: String) {
        Log.d("SYNC", "Iniciando sincronización (room - firebase) para $uid")

        withContext(Dispatchers.IO) {
            val userRef = firestore.collection("users").document(uid)

            // Authors
            db.authorDao().obtenerAutoresOnce().forEach {
                userRef.collection("authors").document(it.id.toString()).set(it)
            }

            // Genres
            db.genreDao().obtenerGenerosOnce().forEach {
                userRef.collection("genres").document(it.id.toString()).set(it)
            }

            // Books
            db.bookDao().obtenerLibrosOnce(uid).forEach { book ->
                val bookRef = userRef.collection("books").document(book.id.toString())
                bookRef.set(book)

                // Notes
                db.noteDao().obtenerNotasPorLibroOnce(book.id).forEach { note ->
                    bookRef.collection("notes")
                        .document(note.id.toString())
                        .set(note)
                }
            }
        }
        Log.d("SYNC", "terminando sincronización (room - firebase) para $uid")
    }

    suspend fun syncPendingChanges(uid: String) {

        Log.d("SYNC", "Iniciando sincronización pendientes para $uid")

        val userRef = firestore.collection("users").document(uid)

        // Books
        val pendingBooks = db.bookDao().getPendingSyncBooks()

        pendingBooks.forEach { book ->
            userRef
                .collection("books")
                .document(book.id.toString())
                .set(book)
                .await()

            db.bookDao().clearSyncPending(book.id)
        }

        // Notes
        val pendingNotes = db.noteDao().getPendingSyncNotes()

        pendingNotes.forEach { note ->
            userRef
                .collection("books")
                .document(note.bookId.toString())
                .collection("notes")
                .document(note.id.toString())
                .set(note)
                .await()

            db.noteDao().clearSyncPending(note.id)
        }

        // Authors
        val pendingAuthors = db.authorDao().getPendingSyncAuthors()

        pendingAuthors.forEach { author ->
            userRef
                .collection("authors")
                .document(author.id.toString())
                .set(author)
                .await()

            db.authorDao().clearSyncPending(author.id)
        }

        // Genres
        val pendingGenres = db.genreDao().getPendingSyncGenres()

        pendingGenres.forEach { genre ->
            userRef
                .collection("genres")
                .document(genre.id.toString())
                .set(genre)
                .await()

            db.genreDao().clearSyncPending(genre.id)
        }

        Log.d("SYNC", "Teminando sincronización pendientes para $uid")

    }

    suspend fun syncPendingDeletes(uid: String) {

        Log.d("SYNC", "Iniciando sincronización deleted para $uid")

        val userRef = firestore.collection("users").document(uid)

        val pendingDeletes = db.pendingDeleteDao().getAll()

        pendingDeletes.forEach { pending ->
            try {
                when (pending.entityType) {

                    "book" -> {
                        userRef
                            .collection("books")
                            .document(pending.entityId.toString())
                            .delete()
                            .await()
                    }

                    "note" -> {
                        userRef
                            .collection("books")
                            .document(pending.parentId.toString())
                            .collection("notes")
                            .document(pending.entityId.toString())
                            .delete()
                            .await()
                    }
                }

                db.pendingDeleteDao().deleteById(pending.id)

            }catch (e: Exception) {
                Log.e("SYNC", "Error borrando ${pending.entityId}: ${e.message}")

                if (e.message?.contains("NOT_FOUND") == true) {
                    db.pendingDeleteDao().deleteById(pending.id)
                }
            }

        }

        Log.d("SYNC", "Teminando sincronización deleted para $uid")

    }

}