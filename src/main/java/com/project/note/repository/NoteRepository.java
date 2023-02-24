package com.project.note.repository;

import com.project.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, String> {
    Optional<Note> findByIdAndAuthor(String id, String author);

    void deleteByIdAndAuthor(String id, String author);
}
