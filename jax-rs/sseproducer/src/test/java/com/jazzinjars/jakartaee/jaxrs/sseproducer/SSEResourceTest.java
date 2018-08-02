package com.jazzinjars.jakartaee.jaxrs.sseproducer;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.jazzinjars.jakartaee.jaxrs.sseproducer.data.EventData;
import com.jazzinjars.jakartaee.jaxrs.sseproducer.producer.SSEResource;
import com.jazzinjars.jakartaee.jaxrs.sseproducer.rest.RestApplication;
import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;
import java.net.URL;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

@RunWith(Arquillian.class)
public class SSEResourceTest {

    private static final String[] EVENT_TYPES = {"INIT", "EVENT", "FINISH"};

    @ArquillianResource
    private URL base;

    private Client sseClient;
    private WebTarget target;
    private SseEventSource eventSource;

    @Deployment
    public static WebArchive createDeployment() {
        return create(WebArchive.class)
                .addClasses(RestApplication.class, SSEResource.class, EventData.class);
    }

    /**
     * Initializes the client, target and the eventSource used to create event consumers
     */
    @Before
    public void setup() {
        this.sseClient = ClientBuilder.newClient();
        this.target = this.sseClient.target(base + "rest/sse/register");
        this.eventSource = SseEventSource.target(target).build();
        System.out.println("SSE Event source created........");
    }

    @After
    public void teardown() {
        this.eventSource.close();
        System.out.println("Closed SSE Event Source..");
        this.sseClient.close();
        System.out.println("Closed JAX-RS client..");
    }

    /**
     * Registers reaction on events, waits for events and checks their content
     * @throws Exception
     */
    @Test(timeout = 5000)
    @RunAsClient
    public void testSSE() throws Exception {

        final Queue<Throwable> asyncExceptions = new ConcurrentLinkedQueue<>();
        final Queue<EventData> receivedEvents = new ConcurrentLinkedQueue<>();
        // jsonb is thread safe!
        final Jsonb jsonb = JsonbBuilder.create();

        final Consumer<InboundSseEvent> onEvent =
                (sseEvent) -> {

                    assertThat("event type", sseEvent.getName(), Matchers.isOneOf(EVENT_TYPES));

                    final String data = sseEvent.readData();
                    System.out.println("Data received as string:\n" + data);

                    assertNotNull("data received as string", data);
                    final EventData event = jsonb.fromJson(data, EventData.class);

                    receivedEvents.add(event);
                    assertThat("event.time", event.getTime(), instanceOf(Date.class));
                    assertNotNull("event.id", event.getId());
                    assertThat("event.comment", event.getComment(), Matchers.containsString("event:"));
        };

        this.eventSource.register(onEvent, asyncExceptions::add);
        System.out.println("Server Side Events Client registered in the test thread.");

        // following line starts acceptation of events.
        this.eventSource.open();

        // don't end the test until we have all events or timeout or error comes.
        // this is not an obvious implementation, we only need to hold the test until all events
        // are asynchronously processed.
        while (receivedEvents.size() <= 5 && asyncExceptions.isEmpty()) {
            Thread.sleep(10L);
        }
        assertThat("receiver exceptions", asyncExceptions, Matchers.emptyIterable());
    }

}
