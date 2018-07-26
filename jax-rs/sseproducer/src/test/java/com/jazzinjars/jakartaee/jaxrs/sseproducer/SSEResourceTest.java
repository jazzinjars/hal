package com.jazzinjars.jakartaee.jaxrs.sseproducer;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.jazzinjars.jakartaee.jaxrs.sseproducer.data.EventData;
import com.jazzinjars.jakartaee.jaxrs.sseproducer.producer.SSEResource;
import com.jazzinjars.jakartaee.jaxrs.sseproducer.rest.RestApplication;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

@RunWith(Arquillian.class)
public class SSEResourceTest {

    @ArquillianResource
    private URL base;

    private Client sseClient;
    private WebTarget target;

    SseEventSource eventSource;

    String[] types = {"INIT", "EVENT", "FINISH"};

    @Deployment
    public static WebArchive createDeployment() {
        return create(WebArchive.class)
                .addClasses(RestApplication.class, SSEResource.class, EventData.class, JsonbBuilder.class, Jsonb.class);
    }

    @BeforeAll
    public void setup() {
        this.sseClient = ClientBuilder.newClient();
        this.target = this.sseClient.target(base + "rest/sse/register");
        eventSource = SseEventSource.target(target).build();
        System.out.println("SSE Event source created........");
    }

    @AfterAll
    public void teardown() {
        eventSource.close();
        System.out.println("Closed SSE Event Source..");
        sseClient.close();
        System.out.println("Closed JAX-RS client..");
    }

    @Test
    @RunAsClient
    public void testSSE() throws IOException {

        Jsonb jsonb = JsonbBuilder.create();

        System.out.println("SSE Client triggered in thread " + Thread.currentThread().getName());
        try {
            eventSource.register(
                    sseEvent -> {
                        assertTrue(Arrays.asList(types).contains(sseEvent.getName()));
                        assertNotNull(sseEvent.readData());

                        EventData eventData = jsonb.fromJson(sseEvent.readData(), EventData.class);
                        assertThat(eventData.getTime(), instanceOf(Date.class));
                        assertNotNull(eventData.getId());
                        assertTrue(eventData.getComment().contains("event:"));

                        System.out.println("\nSSE Event received :: " + eventData.toString() + "\n");

                    },
                    e -> e.printStackTrace()
            );

            eventSource.open();
            Thread.sleep(1500);

        } catch (Exception e) {
            System.out.println("Error on SSE Test");
            System.out.println(e.getMessage());
        }
    }

}
