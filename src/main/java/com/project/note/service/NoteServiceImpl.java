package com.project.note.service;

import com.project.note.dto.AddNoteRequest;
import com.project.note.dto.NoteDto;
import com.project.note.dto.UpdateNoteRequest;
import com.project.note.dto.UserCredentialDto;
import com.project.note.entity.Note;
import com.project.note.exception.ResourceNotFoundException;
import com.project.note.exception.ValidationException;
import com.project.note.repository.NoteRepository;
import com.project.note.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final ValidationUtil validationUtil;

    @Override
    public NoteDto addNote(AddNoteRequest request, UserCredentialDto credential) throws ValidationException {

        validationUtil.validate(request);

        Long currentTimeStamp = Instant.now().toEpochMilli();

        Note note = new Note(
                UUID.randomUUID().toString(),
                request.title(),
                request.body(),
                credential.id(),
                currentTimeStamp,
                currentTimeStamp
        );

        return noteToDto(noteRepository.save(note));
    }

    @Override
    public NoteDto getNoteById(@NonNull String id, UserCredentialDto credential) throws ResourceNotFoundException {
        return noteRepository
                .findByIdAndAuthor(id, credential.id())
                .map(this::noteToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Note with id " + id + " doesn't exist"));
    }

    @Override
    public List<NoteDto> getAllNotes(UserCredentialDto credential) {
        return noteRepository.findAll().stream().map(this::noteToDto).toList();
    }

    @Override
    public NoteDto updateNote(UpdateNoteRequest request, UserCredentialDto credential) throws ResourceNotFoundException, ValidationException {

        validationUtil.validate(request);

        Note note = noteRepository
                .findByIdAndAuthor(request.id(), credential.id())
                .orElseThrow(() -> new ResourceNotFoundException("Note with id " + request.id() + " doesn't exist!"));

        note.setTitle(request.title());
        note.setBody(request.body());
        note.setUpdatedAt(Instant.now().toEpochMilli());

        return noteToDto(noteRepository.save(note));
    }

    @Override
    public String deleteNote(String id, UserCredentialDto credentialDto) throws ResourceNotFoundException {
        Note note = noteRepository
                .findByIdAndAuthor(id, credentialDto.id())
                .orElseThrow(() -> new ResourceNotFoundException("Note with id " + id + "doesn't exist!"));

        noteRepository.deleteByIdAndAuthor(id, credentialDto.id());

        return note.getId();
    }

    private NoteDto noteToDto(Note note) {
        return new NoteDto(
                note.getId(),
                note.getTitle(),
                note.getBody(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }
}
