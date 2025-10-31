package com.notes.app.dto.note;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@Builder
public class NoteTextDto {
    @NonNull
    private String text;
}
