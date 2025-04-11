package com.fii.practic.mes.admin.general.error;

import com.fii.practic.mes.admin.general.AbstractEntity;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.StaleObjectStateException;
import org.hibernate.exception.ConstraintViolationException;
import org.jboss.logging.Logger;

import java.sql.SQLException;
import java.util.Objects;

@Provider
public class ErrorMapper implements ExceptionMapper<Exception> {
    private static final Logger LOGGER = Logger.getLogger(ErrorMapper.class.getName());

    @Override
    public Response toResponse(final Exception exception) {
        LOGGER.error(exception.getLocalizedMessage(), exception);
        
        ApplicationRuntimeException applicationException = null;

        if (exception instanceof WebApplicationException webApplicationException) {
            if (webApplicationException.getCause() instanceof ApplicationRuntimeException applicationRuntimeException) {
                applicationException = applicationRuntimeException;
            } else {
                return webApplicationException.getResponse();
            }
        } else if (exception instanceof PersistenceException persistenceException) {
            applicationException = handlePersistenceException(persistenceException);
        } else if (exception instanceof SQLException sqlException) {
            applicationException = new ApplicationRuntimeException(ServerErrorEnum.UNEXPECTED_EXCEPTION,
                    sqlException.getLocalizedMessage());
        } else {
            applicationException = new ApplicationRuntimeException(ServerErrorEnum.UNEXPECTED_EXCEPTION,
                    exception.getLocalizedMessage());
        }

        return applicationException.getResponse();
    }

    private static ApplicationRuntimeException handleConstraintViolationException(final ConstraintViolationException cex) {
        String constraintName = getConstraintName(cex);

        final ApplicationRuntimeException ex = handleApplicationSpecificErrors(constraintName);
        if (ex != null) {
            return ex;
        }

        if (!StringUtils.isEmpty(constraintName) && constraintName.contains("FK")) {
            return new ApplicationRuntimeException(ServerErrorEnum.FK_CONSTRAINT_VIOLATION_UNKNOWN, constraintName);
        }

        return new ApplicationRuntimeException(ServerErrorEnum.CONSTRAINT_VIOLATION_UNKNOWN, constraintName);
    }

    private static String getConstraintName(ConstraintViolationException cex) {
        String constraint = cex.getConstraintName();
        if (!StringUtils.isEmpty(constraint) && constraint.contains(".")) {
            return constraint.substring(constraint.indexOf(".") + 1);
        }

        constraint = getWordContainingSubStringFromText("FK_", cex.getLocalizedMessage());
        if (!StringUtils.isEmpty(constraint)) {
            return constraint;
        }

        String tableName = getQueriedTableName(cex.getSQL());
        if (!StringUtils.isEmpty(tableName)) {
            constraint = getWordContainingSubStringFromText(tableName, cex.getLocalizedMessage());
        }

        return !StringUtils.isEmpty(constraint) ? constraint : "N/A";
    }

    private static String getQueriedTableName(String sql) {
        String[] splitSql = sql.split(" ");
        for (int idx = 0; idx < splitSql.length; idx++) {
            if (Objects.equals("FROM", splitSql[idx].toUpperCase())
                    ||(Objects.equals("UPDATE", splitSql[idx].toUpperCase())) ) {
                return splitSql[idx + 1];
            }
            if (Objects.equals("INSERT", splitSql[idx].toUpperCase())
                    && idx + 1 < splitSql.length
                    && Objects.equals("INTO", splitSql[idx+1].toUpperCase())) {
                return splitSql[idx + 2];
            }
        }

        return StringUtils.EMPTY;
    }

    private static String getWordContainingSubStringFromText(String subString, String text) {
        String[] splitSql = text.split(" ");
        String substringUpperCase = subString.toUpperCase();
        for (String sqlWord : splitSql) {
            if (sqlWord.toUpperCase().contains(substringUpperCase)) {
                return sqlWord;
            }
        }

        return StringUtils.EMPTY;
    }

    private static ApplicationRuntimeException handleApplicationSpecificErrors(final String constraintName) {
       // TODO: Add specific exception in case
        return switch (constraintName) {
            case "SPECIFIC_CONSTRAINT_NAME" -> new ApplicationRuntimeException(ServerErrorEnum.FK_SPECIFIC_CONSTRAINT_NAME_RECEIVED);
            default -> {
                if (constraintName.endsWith("_NAME")) {
                    yield new ApplicationRuntimeException(ServerErrorEnum.FK_NAME_ALREADY_IN_USE);
                } else if (constraintName.endsWith("_UUID")) {
                    yield new ApplicationRuntimeException(ServerErrorEnum.FK_UUID_ALREADY_IN_USE);
                }
                yield null;
            }
        };


    }

    private ApplicationRuntimeException handlePersistenceException(final PersistenceException ex) {
        Throwable cause = ex;
        while (cause != null) {
            if (cause instanceof ConstraintViolationException constraintViolationException) {
                return handleConstraintViolationException(constraintViolationException);
            } else if (cause instanceof StaleObjectStateException staleObjectStateException) {
                return handleStaleObjectStateException(staleObjectStateException);
            } else if (cause instanceof OptimisticLockException lockException) {
                return handleOptimisticLockException(lockException);
            }
            cause = cause.getCause();
        }

        return new ApplicationRuntimeException(ServerErrorEnum.UNEXPECTED_EXCEPTION,
                Objects.nonNull(ex) ? ex.getLocalizedMessage() : StringUtils.EMPTY);
    }

    private ApplicationRuntimeException handleStaleObjectStateException(
            final StaleObjectStateException staleException) {

        return new ApplicationRuntimeException(ServerErrorEnum.OPTIMISTIC_LOCK_ERROR, staleException.getEntityName(),
                staleException.getIdentifier().toString());
    }

    private ApplicationRuntimeException handleOptimisticLockException(final OptimisticLockException lockException) {
        if (lockException.getEntity() instanceof AbstractEntity entity) {
            String name = "";
            if (Objects.nonNull(entity.getName())) {
                name = entity.getName();
            }
            final String entityId = String.valueOf(entity.getUuid());
            final String entityClassIdAndName = " " + entity.getClass() + " , id=" + entityId + ", name=" + name;
            LOGGER.error("Optimistic lock exception " + entityClassIdAndName, lockException);

            if (!name.isEmpty()) {
                return new ApplicationRuntimeException(ServerErrorEnum.OPTIMISTIC_LOCK_ERROR_NAMED,
                        entity.getClass().getName(), name);
            }
            return new ApplicationRuntimeException(ServerErrorEnum.OPTIMISTIC_LOCK_ERROR_ID,
                    entity.getClass().getName(), entityId);
        }

        return new ApplicationRuntimeException(ServerErrorEnum.OPTIMISTIC_LOCK_ERROR,
                lockException.getEntity().getClass().getName());
    }
}
