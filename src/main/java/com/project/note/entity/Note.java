package com.project.note.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notes")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    @Setter
    private String title;
    @Setter
    private String body;
    private String author;
    @Setter
    private Long createdAt;
    @Setter
    private Long updatedAt;
}
