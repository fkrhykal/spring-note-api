package com.project.note.controller;

import com.project.note.dto.AddNoteRequest;
import com.project.note.dto.NoteDto;
import com.project.note.dto.SuccessResponse;
import com.project.note.dto.UserCredentialDto;
import com.project.note.exception.ResourceNotFoundException;
import com.project.note.exception.ValidationException;
import com.project.note.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/notes")
    public ResponseEntity<SuccessResponse<NoteDto>> addNote(
            @RequestBody AddNoteRequest requestBody,
            @RequestAttribute("User-Credential") UserCredentialDto credential
    ) throws ValidationException {
        return SuccessResponse.ok(noteService.addNote(requestBody, credential)).toResponseEntity();
    }

    @GetMapping("/notes")
    public ResponseEntity<SuccessResponse<List<NoteDto>>> getAllNotes(@RequestAttribute("User-Credential") UserCredentialDto credential) {
        return SuccessResponse.ok(noteService.getAllNotes(credential)).toResponseEntity();
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<SuccessResponse<NoteDto>> getNoteById(
            @PathVariable("id") String id,
            @RequestAttribute("User-Credential") UserCredentialDto credential
    ) throws ResourceNotFoundException {
        return SuccessResponse.ok(noteService.getNoteById(id, credential)).toResponseEntity();
    }
}
