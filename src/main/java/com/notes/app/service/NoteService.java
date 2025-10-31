package com.notes.app.service;

import com.notes.app.domain.MongoNote;
import com.notes.app.dto.note.*;
import com.notes.app.domain.enums.Tag;
import com.notes.app.exception.NoteNotFound;
import com.notes.app.mapper.NoteMapper;
import com.notes.app.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.notes.app.utils.Utils.getCurrentUserId;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public NoteResponseDto getById(String id) {
        MongoNote note = noteRepository.findById(id, getCurrentUserId());
        if (note == null)
            throw new NoteNotFound("Note not found with id " + id);

        return NoteMapper.toDto(note);
    }

    public NoteStatDto getStatsById(@PathVariable String id) {
        Map<String, Integer> stats =
                Arrays.stream(noteRepository.getTextOfNoteById(id, getCurrentUserId()).getText().split("\\W+"))
                        .collect(Collectors.toMap(
                            w -> w,
                            w -> 1,
                            Integer::sum,
                            () -> new TreeMap<String, Integer>(Comparator.reverseOrder())
                        ));

        return NoteStatDto.builder().stats(stats).build();
    }

    public NoteTextDto getNoteText(@PathVariable String id) {
        return NoteMapper.toTextDto(noteRepository.getTextOfNoteById(id, getCurrentUserId()));
    }

    public NoteResponseDto create(@RequestBody NoteRequestDto note) {
        MongoNote createdNote = noteRepository.save(NoteMapper.toMongo(note));
        return NoteMapper.toDto(createdNote);
    }

    public List<NoteResponseDto> findAllByAuthorId(int page, int size, Tag tag) {
        return noteRepository.findAllByAuthorId(getCurrentUserId(), page, size, tag)
                .stream()
                .map(NoteMapper::toDto)
                .toList();
    }

    public NoteResponseDto update(@PathVariable String id, @RequestBody NotePatchDto patch) {
        MongoNote note = noteRepository.findById(id, getCurrentUserId());

        if (note == null)
            throw new NoteNotFound("Note not found with id " + id);

        MongoNote updateNote = noteRepository.update(id, getCurrentUserId(), patch);

        return NoteMapper.toDto(updateNote);
    }

    public void deleteById(@PathVariable String id) {
        noteRepository.delete(id, getCurrentUserId());
    }
}
