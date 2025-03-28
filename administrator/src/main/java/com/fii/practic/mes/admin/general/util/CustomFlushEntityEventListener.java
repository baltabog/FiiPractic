package com.fii.practic.mes.admin.general.util;

import com.fii.practic.mes.admin.general.AbstractEntity;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.Status;
import org.hibernate.event.internal.DefaultFlushEntityEventListener;
import org.hibernate.event.spi.FlushEntityEvent;

public class CustomFlushEntityEventListener extends DefaultFlushEntityEventListener {
	private static final String UPDATED_BY_PROPERTY = "updatedBy";

	@Override
	protected void dirtyCheck(final FlushEntityEvent event) throws HibernateException {
		if (event.getEntity() instanceof AbstractEntity entity) {
			super.dirtyCheck(event);

			if (Status.DELETED == event.getEntityEntry().getStatus()) {
				final String[] propertyNames = event.getEntityEntry().getPersister().getPropertyNames();
				final int[] dirtyProperties = event.getDirtyProperties();
				if (dirtyProperties!=null && dirtyProperties.length==1 && UPDATED_BY_PROPERTY.equals(propertyNames[dirtyProperties[0]])) {
					event.setDirtyProperties(null);
					// set updatedBy from the entity to have the user who deleted it in the database
					event.getPropertyValues()[dirtyProperties[0]] = entity.getUpdatedBy();
				}
				return;
			}
			final int[] dirtyProperties = event.getDirtyProperties();
			if (dirtyProperties == null || dirtyProperties.length != 1) {
				return;
			}
			final String[] propertyNames = event.getEntityEntry().getPersister().getPropertyNames();
			if (UPDATED_BY_PROPERTY.equals(propertyNames[dirtyProperties[0]])) {
				// set updatedBy from the database
				entity.setUpdatedBy((String)event.getEntityEntry().getLoadedState()[dirtyProperties[0]]);
				event.setDirtyProperties(null);
			}
		} else {
			super.dirtyCheck(event);
		}
	}

}
