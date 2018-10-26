package com.jazzinjars.jakartaee.jaxrs.reactive.client;

import com.jazzinjars.jakartaee.jaxrs.reactive.model.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

public class JerseyClientLiveTest {

    public static final int HTTP_CREATED = 201;
    private RestClient restClient = new RestClient();

    @Test
    public void givenCorrectObject_whenCorrectJsonRequest_thenResponseCodeCreated() {
	Employee emp = new Employee(6, "Daenerys");
	Response response = restClient.createJsonEmployee(emp);
	Assertions.assertEquals(response.getStatus(), HTTP_CREATED);
    }

    @Test
    public void givenCorrectObject_whenCorrectXmlRequest_thenResponseCodeCreated() {
	Employee emp = new Employee(7, "Catelyn");
	Response response = restClient.createXmlEmployee(emp);
	Assertions.assertEquals(response.getStatus(), HTTP_CREATED);
    }

    @Test
    public void givenCorrectId_whenCorrectJsonRequest_thenCorrectEmployeeRetrieved() {
	int employeeId = 1;
	Employee emp = restClient.getJsonEmployee(employeeId);
	Assertions.assertEquals(emp.getName(), "Arya");
    }

    @Test
    public void givenCorrectId_whenCorrectXmlRequest_thenCorrectEmployeeRetrieved() {
	int employeeId = 1;
	Employee emp = restClient.getXmlEmployee(employeeId);
	Assertions.assertEquals(emp.getName(), "Arya");
    }
}
