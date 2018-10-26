package com.jazzinjars.jakartaee.jaxrs.reactive.client;

import com.jazzinjars.jakartaee.jaxrs.reactive.model.Employee;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClient {

    private static final String REST_URI = "http://localhost:8082/reactive-jaxrs/resources/employees";
    private Client client = ClientBuilder.newClient();

    public Response createJsonEmployee(Employee employee) {
        return client.target(REST_URI).request(MediaType.APPLICATION_JSON).post(Entity.entity(employee, MediaType.APPLICATION_JSON));
    }

    public Employee getJsonEmployee(int id) {
        return client.target(REST_URI).path(String.valueOf(id)).request(MediaType.APPLICATION_JSON).get(Employee.class);
    }

    public Response createXmlEmployee(Employee employee) {
        return client.target(REST_URI).request(MediaType.APPLICATION_XML).post(Entity.entity(employee, MediaType.APPLICATION_XML));
    }

    public Employee getXmlEmployee(int id) {
        return client.target(REST_URI).path(String.valueOf(id)).request(MediaType.APPLICATION_XML).get(Employee.class);
    }

}
