package com.jazzinjars.jakartaee.cdi.bean.discover;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import static org.junit.Assert.assertTrue;

public class CdiDisabledTest {

	@Inject
	BeanManager beanManager;

	@Deployment
	public static Archive<?> deploy() {
		return ShrinkWrap.create(WebArchive.class).addClasses(CdiDisabledBean.class, CdiEnabledBean.class)
				.addAsWebInfResource("none-beans.xml", "beans.xml");
	}

	@Test
	public void should_beans_be_injected() throws Exception {
		assertTrue("BeanManager shouldn't be present for a CDI disabled archive.", beanManager == null);
	}
}
