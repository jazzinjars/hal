package com.jazzinjars.jakartaee.cdi.bean.discover.all;

import com.jazzinjars.jakartaee.cdi.bean.discover.disabled.CdiDisabledBean;
import com.jazzinjars.jakartaee.cdi.bean.discover.enabled.CdiEnabledBean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.Set;

import static org.junit.Assert.assertFalse;

@RunWith(Arquillian.class)
public class CdiEnabledTest {

	@Inject
	BeanManager beanManager;

	@Deployment
	public static Archive<?> deploy() {
		return ShrinkWrap.create(WebArchive.class).addClasses(CdiDisabledBean.class, CdiEnabledBean.class)
				.addAsWebInfResource("all-beans.xml", "beans.xml");
	}

	@Test
	public void should_beans_be_injected() throws Exception {
		Set<Bean<?>> disabledBeans = beanManager.getBeans(CdiDisabledBean.class);
		assertFalse("Instances of disabled bean expected.", disabledBeans.isEmpty());

		Set<Bean<?>> enabledBeans = beanManager.getBeans(CdiEnabledBean.class);
		assertFalse("Instances of enabled bean expected.", enabledBeans.isEmpty());
	}
}
