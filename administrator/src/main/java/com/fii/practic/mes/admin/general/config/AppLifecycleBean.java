// Copyright by camLine GmbH, Germany, 2022
package com.fii.practic.mes.admin.general.config;

import com.fii.practic.mes.admin.general.util.CustomFlushEntityEventListener;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnit;
import jakarta.transaction.Transactional;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AppLifecycleBean {

	private static final Logger LOGGER = Logger.getLogger(AppLifecycleBean.class);

	@PersistenceUnit
	EntityManager entityManager;

	@SuppressWarnings("unused")
	@Transactional
 	void onStart(@Observes final StartupEvent ev) {
		LOGGER.info("AppLifecycleBean observed StartupEvent");

		final SessionFactoryImpl sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class);
		final EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
		if (registry != null) {
			registry.getEventListenerGroup(EventType.FLUSH_ENTITY).clearListeners();
			registry.getEventListenerGroup(EventType.FLUSH_ENTITY).appendListener(new CustomFlushEntityEventListener());
		}
	 }

}
