package com.jazzinjars.jakartaee.jaxrs.reactive.clientrx;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class ClientOrchestrationIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(ClientOrchestrationIntegrationTest.class);
    private Client client = ClientBuilder.newClient();

    private WebTarget userIdService = client.target("http://localhost:8080/id-service/ids");
    private WebTarget nameService = client.target("http://localhost:8080/name-service/users/{userId}/name");
    private WebTarget hashService = client.target("http://localhost:8080/hash-service/{rawValue}");

    private String expectedUserIds = "[1,2,3,4,5,6]";
    private List<String> expectedNames = Arrays
	    .asList("n/a", "Thor", "Hulk", "BlackWidow", "BlackPanther", "TheTick", "Hawkeye");
    private List<String> expectedHashValues = Arrays.asList("roht1", "kluh2", "WodiwKcalb3", "RehtnapKclab4", "kciteht5", "eyekwah6");

    @Rule
    public WireMockRule wireMockServer = new WireMockRule();

    @Before
    public void setup() {
        stubFor(get(urlEqualTo("/id-service/ids")).willReturn(aResponse().withBody(expectedUserIds).withHeader("Content-Type", "application(json")));

        for (int i = 1; i <= 6; i++) {
            stubFor(get(urlEqualTo("/name-service/users/" + i + "/name")).willReturn(aResponse().withBody(expectedNames.get(i))));
        }
        stubFor(get(urlEqualTo("/hash-service/Thor1")).willReturn(aResponse().withBody(expectedHashValues.get(0))));
        stubFor(get(urlEqualTo("/hash-service/Hulk2")).willReturn(aResponse().withBody(expectedHashValues.get(1))));
        stubFor(get(urlEqualTo("/hash-service/BlackWidow3")).willReturn(aResponse().withBody(expectedHashValues.get(2))));
        stubFor(get(urlEqualTo("/hash-service/BlackPanther4")).willReturn(aResponse().withBody(expectedHashValues.get(3))));
        stubFor(get(urlEqualTo("/hash-service/TheTick5")).willReturn(aResponse().withBody(expectedHashValues.get(4))));
        stubFor(get(urlEqualTo("/hash-service/Hawkeye6")).willReturn(aResponse().withBody(expectedHashValues.get(5))));
    }

    @Test
    public void callBackOrchestrate() throws InterruptedException {
        List<String> receivedHashValues = new ArrayList<>();

        // used to keep track of the progress of the subsequent calls
        final CountDownLatch completionTracker = new CountDownLatch(expectedHashValues.size());
    }
}
