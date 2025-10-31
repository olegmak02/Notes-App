package com.notes.app.dto.note;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.Date;

@Data
@Builder
public class NoteResponseDto {
    private String title;
    private Date createdDate;
}
