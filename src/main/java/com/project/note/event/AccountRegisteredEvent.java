package com.project.note.event;

public record AccountRegisteredEvent(
    String id,
    String firstname,
    String lastname,
    String email,
    String verificationCode
) {
}
