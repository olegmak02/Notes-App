package com.notes.app.dto.note;

import com.notes.app.domain.enums.Tag;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@Builder
public class NotePatchDto {
    @Id
    private ObjectId id;
    private String title;
    private Date createdDate;
    private String text;
    private Tag tag;
}
