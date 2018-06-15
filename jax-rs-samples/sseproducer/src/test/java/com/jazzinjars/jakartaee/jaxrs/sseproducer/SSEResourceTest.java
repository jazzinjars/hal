package com.jazzinjars.jakartaee.jaxrs.sseproducer;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;

import com.jazzinjars.jakartaee.jaxrs.sseproducer.data.EventData;
import com.jazzinjars.jakartaee.jaxrs.sseproducer.producer.SSEResource;
import com.jazzinjars.jakartaee.jaxrs.sseproducer.rest.RestApplication;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;
import java.net.URL;

@RunWith(Arquillian.class)
public class SSEResourceTest {

    @ArquillianResource
    private URL base;

    private Client sseClient;
    private WebTarget target;

    SseEventSource eventSource;

    @Deployment
    public static WebArchive createDeployment() {
        return create(WebArchive.class)
                .addClasses(RestApplication.class, SSEResource.class, EventData.class, JsonbBuilder.class, Jsonb.class);
    }

}
