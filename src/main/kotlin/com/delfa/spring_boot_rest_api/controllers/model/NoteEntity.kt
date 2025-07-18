package com.delfa.spring_boot_rest_api.controllers.model

import com.delfa.spring_boot_rest_api.database.model.Note
import org.bson.types.ObjectId
import java.time.Instant

data class NoteRequest(
    val id: String?,
    val title: String,
    val content: String,
    val color: Long,
    val ownerId: String
)

data class NoteResponse(
    val id: String,
    val title: String,
    val content: String,
    val color: Long,
    val createdAt: Instant
)

fun NoteRequest.toNote(): Note {
    return Note(
        id = id?.let { ObjectId(it) } ?: ObjectId.get(),
        title = title,
        content = content,
        color = color,
        createdAt = Instant.now(),
        ownerId = ObjectId(ownerId)
    )
}