package com.delfa.spring_boot_rest_api.controllers

import com.delfa.spring_boot_rest_api.controllers.model.NoteRequest
import com.delfa.spring_boot_rest_api.controllers.model.NoteResponse
import com.delfa.spring_boot_rest_api.controllers.model.toNote
import com.delfa.spring_boot_rest_api.database.model.toNoteResponse
import com.delfa.spring_boot_rest_api.database.repository.NoteRepository
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*

//POST http://localhost:8085/notes
//GET http://localhost:8085/notes?ownerId=60c72b2f9b1d8c001c8e4f3a

@RestController
@RequestMapping("/api/notes")
class NoteController(
    private val noteRepository: NoteRepository
) {

    @PostMapping
    fun save(
        body: NoteRequest
    ): NoteResponse = noteRepository.save(body.toNote()).toNoteResponse()

    @GetMapping
    fun findByOwnerId(
        @RequestParam(required = true) ownerId: String
    ): List<NoteResponse> = noteRepository.findByOwnerId(ObjectId(ownerId)).map { it.toNoteResponse() }

}