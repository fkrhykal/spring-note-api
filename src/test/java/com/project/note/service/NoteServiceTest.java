package com.project.note.service;

import com.project.note.dto.AddNoteRequest;
import com.project.note.dto.NoteDto;
import com.project.note.dto.UpdateNoteRequest;
import com.project.note.dto.UserCredentialDto;
import com.project.note.entity.Note;
import com.project.note.exception.ResourceNotFoundException;
import com.project.note.exception.ValidationException;
import com.project.note.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class NoteServiceTest {
    @Autowired
    private NoteService noteService;
    @MockBean
    private NoteRepository noteRepository;
    private UserCredentialDto userCredential;

    @BeforeEach
    public void setUp() {
        userCredential = new UserCredentialDto(
                UUID.randomUUID().toString(),
                "John",
                "Doe",
                "abc@example.com"
        );
        doAnswer(returnsFirstArg()).when(noteRepository).save(any(Note.class));
    }

    @Test
    public void addNote() throws ValidationException {


        NoteDto noteDto = noteService.addNote(
                new AddNoteRequest(
                        "title",
                        "body"
                ),
                userCredential
        );

        assertEquals("title", noteDto.title());
    }

    @Test
    public void whenAddNoteRequestInvalid_throwValidationException() {
        assertThrows(ValidationException.class, () -> {
            noteService.addNote(
                    new AddNoteRequest(
                            "",
                            ""
                    ),
                    userCredential
            );
        });
    }


    @Test
    public void getNote() throws ResourceNotFoundException {

        String id = UUID.randomUUID().toString();
        String title = "title";
        String body = "body";
        Long now = Instant.now().toEpochMilli();

        doReturn(Optional.of(
                new Note(
                        id,
                        title,
                        body,
                        userCredential.email(),
                        now,
                        now
                )
        )).when(noteRepository).findByIdAndAuthor(id, userCredential.id());

        NoteDto noteDto = noteService.getNoteById(
                id, userCredential
        );

        assertEquals(title, noteDto.title());
    }

    @Test
    public void whenGetNoteRequestIdDidNotMatchAnyNote_throwResourceNotFoundException() {
        String id = UUID.randomUUID().toString();
        doReturn(Optional.empty()).when(noteRepository).findByIdAndAuthor(id, userCredential.id());
        assertThrows(ResourceNotFoundException.class, () -> {
            noteService.getNoteById(
                    id,
                    userCredential
            );
        });
    }


    @Test
    public void deleteDto() throws ResourceNotFoundException {

        String id = UUID.randomUUID().toString();
        String title = "title";
        String body = "body";
        Long now = Instant.now().toEpochMilli();

        doReturn(Optional.of(
                new Note(
                        id,
                        title,
                        body,
                        userCredential.email(),
                        now,
                        now
                )
        )).when(noteRepository).findByIdAndAuthor(id, userCredential.id());

        String deletedId = noteService.deleteNote(
                id,
                userCredential
        );

        assertEquals(id, deletedId);
    }

    @Test
    public void whenDeleteNoteRequestIdDidNotMatchAnyNote_throwResourceNotFoundException() {
        String id = UUID.randomUUID().toString();
        doReturn(Optional.empty()).when(noteRepository).findByIdAndAuthor(id, userCredential.id());
        assertThrows(ResourceNotFoundException.class, () -> {
            noteService.deleteNote(id, userCredential);
        });
    }

    @Test
    public void updateNote() throws ResourceNotFoundException, ValidationException {
        String id = UUID.randomUUID().toString();
        String title = "title";
        String body = "body";
        Long now = Instant.now().toEpochMilli();

        doReturn(Optional.of(
                new Note(
                        id,
                        title,
                        body,
                        userCredential.email(),
                        now,
                        now
                )
        )).when(noteRepository).findByIdAndAuthor(id, userCredential.id());

        NoteDto noteDto = noteService.updateNote(
                new UpdateNoteRequest(
                        id,
                        "updated title",
                        "updated body"
                ),
                userCredential
        );
        assertEquals("updated title", noteDto.title());
    }

    @Test
    public void whenUpdateNoteRequestDidNotMatchAnyNote_throwResourceNotFoundException() {
        String id = UUID.randomUUID().toString();
        doReturn(Optional.empty()).when(noteRepository).findByIdAndAuthor(id, userCredential.id());
        assertThrows(ResourceNotFoundException.class, () -> {
            noteService.updateNote(
                    new UpdateNoteRequest(
                            id,
                            "updated title",
                            "updated body"
                    ),
                    userCredential
            );
        });
    }

    @Test
    public void whenUpdateNoteRequestInvalid_throwValidationException() {
        assertThrows(ValidationException.class, () -> {
            noteService.updateNote(
                    new UpdateNoteRequest(
                            "", "", ""
                    ),
                    userCredential
            );
        });
    }
}