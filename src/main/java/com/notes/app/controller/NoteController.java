package com.notes.app.controller;

import com.notes.app.domain.enums.Tag;
import com.notes.app.dto.note.*;
import com.notes.app.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/note")
public class NoteController {

    @Autowired
    private NoteService service;

    @GetMapping("/{id}")
    public NoteResponseDto getById(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping("/{id}/stats")
    public NoteStatDto getStats(@PathVariable String id) {
        return service.getStatsById(id);
    }

    @GetMapping("/{id}/text")
    public NoteTextDto getNoteText(@PathVariable String id) {
        return service.getNoteText(id);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponseDto create(@RequestBody NoteRequestDto note) {
        return service.create(note);
    }

    @GetMapping("/list")
    public List<NoteResponseDto> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Tag tag
    ) {
        return service.findAllByAuthorId(page, size, tag);
    }

    @PatchMapping("/update/{id}")
    public NoteResponseDto update(@PathVariable String id, @RequestBody NotePatchDto note) {
        return service.update(id, note);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }
}
