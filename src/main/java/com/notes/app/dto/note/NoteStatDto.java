package com.notes.app.dto.note;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class NoteStatDto {
    private Map<String, Integer> stats = new HashMap<>();
}
