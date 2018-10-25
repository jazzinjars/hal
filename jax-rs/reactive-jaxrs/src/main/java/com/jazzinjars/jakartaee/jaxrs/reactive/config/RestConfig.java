package com.jazzinjars.jakartaee.jaxrs.reactive.config;

import com.jazzinjars.jakartaee.jaxrs.reactive.exceptions.AlreadyExistsExceptionHandler;
import com.jazzinjars.jakartaee.jaxrs.reactive.exceptions.NotFoundExceptionHandler;
import com.jazzinjars.jakartaee.jaxrs.reactive.rest.EmployeeResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/resources")
public class RestConfig extends Application {

    public Set<Class<?>> getClasses() {
	return new HashSet<>(
		Arrays.asList(EmployeeResource.class, NotFoundExceptionHandler.class, AlreadyExistsExceptionHandler.class));
    }
}
