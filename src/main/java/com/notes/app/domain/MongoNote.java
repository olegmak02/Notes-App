package com.notes.app.domain;

import com.notes.app.domain.enums.Tag;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "notes")
public class MongoNote {
    @Id
    private ObjectId id;
    private String authorId;
    private String title;
    private Date createdDate;
    private String text;
    private Tag tag;
}
