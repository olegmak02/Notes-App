package com.notes.app.dto.note;

import com.notes.app.domain.enums.Tag;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.Date;

@Data
@Builder
public class NoteRequestDto {
    @NonNull
    private String title;
    @NonNull
    private String text;
    private Tag tag;
}
