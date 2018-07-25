package com.jazzinjars.jakartaee.jaxrs.sseproducer.data;

import lombok.Data;

import javax.json.bind.JsonbBuilder;
import java.util.Date;
import java.util.UUID;

@Data
public class EventData {

    private Date time;
    private String id;
    private String comment;

    public EventData(String comment) {
        this.setTime(new Date());
        this.setId(UUID.randomUUID().toString());
        this.setComment(comment);
    }

    @Override
    public String toString() {
       return JsonbBuilder.create().toJson(this);
    }

}
