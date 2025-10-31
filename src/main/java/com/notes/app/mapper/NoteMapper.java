package com.notes.app.mapper;

import com.notes.app.domain.MongoNote;
import com.notes.app.dto.note.NoteRequestDto;
import com.notes.app.dto.note.NoteResponseDto;
import com.notes.app.dto.note.NoteTextDto;

import java.util.Date;

import static com.notes.app.utils.Utils.getCurrentUserId;

public class NoteMapper {
    public static MongoNote toMongo(NoteRequestDto note) {
        return MongoNote.builder()
                .title(note.getTitle())
                .text(note.getText())
                .createdDate(new Date())
                .tag(note.getTag())
                .authorId(getCurrentUserId())
                .build();
    }

    public static NoteResponseDto toDto(MongoNote note) {
        String title = note.getTitle() == null ? "" : note.getTitle();

        if (note.getCreatedDate() == null)
            throw new IllegalArgumentException("Created date is null");

        return NoteResponseDto.builder()
                .title(title)
                .createdDate(note.getCreatedDate())
                .build();
    }

    public static NoteTextDto toTextDto(MongoNote note) {
        String text = note.getText() == null ? "" : note.getText();

        return NoteTextDto.builder()
                .text(text)
                .build();
    }
}
