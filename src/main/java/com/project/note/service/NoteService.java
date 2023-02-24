package com.project.note.service;

import com.project.note.dto.AddNoteRequest;
import com.project.note.dto.NoteDto;
import com.project.note.dto.UpdateNoteRequest;
import com.project.note.dto.UserCredentialDto;
import com.project.note.exception.ResourceNotFoundException;
import com.project.note.exception.ValidationException;

import java.util.List;

public interface NoteService {
    NoteDto addNote(AddNoteRequest request, UserCredentialDto credential) throws ValidationException;

    NoteDto getNoteById(String id, UserCredentialDto credential) throws ResourceNotFoundException;

    List<NoteDto> getAllNotes(UserCredentialDto credential);

    NoteDto updateNote(UpdateNoteRequest request, UserCredentialDto credential) throws ResourceNotFoundException, ValidationException;

    String deleteNote(String id, UserCredentialDto credentialDto) throws ResourceNotFoundException;
}