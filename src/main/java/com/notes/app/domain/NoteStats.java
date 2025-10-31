package com.notes.app.domain;

import org.bson.types.ObjectId;

import java.util.HashMap;

import java.util.Map;

public class NoteStats {
    private ObjectId id;
    private Map<String, Integer> stats = new HashMap<String, Integer>();
}
