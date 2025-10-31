package com.notes.app.repository;

import com.notes.app.domain.MongoNote;
import com.notes.app.domain.enums.Tag;
import com.notes.app.dto.note.NotePatchDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoteRepository {

    private MongoTemplate mongoTemplate;

    public NoteRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public MongoNote findById(String id, String authorId) {
        Query query = new Query().addCriteria(createCriteriaForIdAndAuthorId(id, authorId));
        return mongoTemplate.findOne(query, MongoNote.class);
    }

    public MongoNote getTextOfNoteById(String id, String authorId) {
        Query query = new Query();
        query.addCriteria(createCriteriaForIdAndAuthorId(id, authorId));
        query.fields().include("text");
        return mongoTemplate.findOne(query, MongoNote.class);
    }

    public MongoNote save(MongoNote note) {
        return mongoTemplate.save(note);
    }

    public List<MongoNote> findAllByAuthorId(String authorId, int page, int size, Tag tag) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Query query = new Query(Criteria.where("authorId").is(authorId));

        if (tag != null) {
            query.addCriteria(Criteria.where("tag").is(tag));
        }

        query.with(pageable);
        return mongoTemplate.find(query, MongoNote.class);
    }

    public MongoNote update(String id, String authorId, NotePatchDto note) {
        Query query = new Query(createCriteriaForIdAndAuthorId(id, authorId));
        Update update = new Update();

        if (note.getTag() != null) {
            update.set("tag", note.getTag());
        }

        if (note.getTitle() != null) {
            update.set("title", note.getTitle());
        }

        if (note.getCreatedDate() != null) {
            update.set("createdDate", note.getCreatedDate());
        }

        if (note.getText() != null) {
            update.set("text", note.getText());
        }

        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        return mongoTemplate.findAndModify(query, update, options, MongoNote.class);
    }

    public void delete(String id, String authorId) {
        Query query = new Query(createCriteriaForIdAndAuthorId(id, authorId));
        mongoTemplate.remove(query, MongoNote.class, "notes");
    }

    private Criteria createCriteriaForIdAndAuthorId(String id, String authorId) {
        return Criteria.where("_id").is(id).and("authorId").is(authorId);
    }
}
