package com.jazzinjars.jakartaee.jaxrs.sseproducer.producer;

import com.jazzinjars.jakartaee.jaxrs.sseproducer.data.EventData;

import javax.annotation.PostConstruct;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

@Path("sse")
public class SSEResource {

    @Context
    private Sse sse;

    private volatile SseBroadcaster sseBroadcaster;

    @PostConstruct
    public void init() {
        this.sseBroadcaster = sse.newBroadcaster();
    }

    @GET
    @Path("register")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void register(@Context SseEventSink eventSink) {

        final Jsonb jsonb = JsonbBuilder.create();
        eventSink.send(sse.newEvent("INIT", jsonb.toJson(new EventData("event:initialized"))));
        sseBroadcaster.register(eventSink);

        for (int i = 0; i < 5; i++) {
            sseBroadcaster.broadcast(sse.newEvent("EVENT", jsonb.toJson(new EventData("event:" + 1))));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        eventSink.send(sse.newEvent("FINISH", jsonb.toJson(new EventData("event:finished"))));
    }

}
