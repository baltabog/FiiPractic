// Copyright by camLine GmbH, Germany, 2022
package com.fii.practic.mes.wip.general;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

public abstract class AbstractResource {
	private static final Logger LOGGER = Logger.getLogger(AbstractResource.class.getName());

	@Inject
	MeterRegistry registry;

	/**
	 * Register a timer with standard percentiles
	 *
	 * @return timer
	 */
	protected Timer registerTimer(final String timerName) {
		LOGGER.debug("Register timer: " + timerName);
		return Timer.builder(timerName)
				.publishPercentiles(0.1, 0.25, 0.5, 0.75, 0.95, 0.99)
				.register(registry);
	}
}
