package com.delfa.spring_boot_rest_api.controllers.note

import com.delfa.spring_boot_rest_api.controllers.note.model.NoteRequest
import com.delfa.spring_boot_rest_api.controllers.note.model.NoteResponse
import com.delfa.spring_boot_rest_api.database.repository.NoteRepository
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

//POST http://localhost:8085/notes
//GET http://localhost:8085/notes?ownerId=60c72b2f9b1d8c001c8e4f3a
//DELETE http://localhost:8085/notes/{id}

@RestController
@RequestMapping("/notes")
class NoteController(
    private val noteRepository: NoteRepository
) {

    @PostMapping
    fun save(
        @Valid @RequestBody body: NoteRequest
    ): NoteResponse {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        return noteRepository.save(
            body.toNote().copy(
                ownerId = ObjectId(ownerId)
            )
        ).toNoteResponse()
    }

    @GetMapping
    fun findByOwnerId(): List<NoteResponse> {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        return noteRepository.findByOwnerId(ObjectId(ownerId)).map { it.toNoteResponse() }
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(
        @PathVariable(required = true) id: String
    ) {
        val note = noteRepository.findById(ObjectId(id))
            .orElseThrow { IllegalArgumentException("Note not found") }
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        if (note.ownerId.toHexString() == ownerId) {
            noteRepository.deleteById(ObjectId(id))
        }
    }
}