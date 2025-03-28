package com.fii.practic.mes.wip.general.error;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.ArrayUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApplicationRuntimeException extends WebApplicationException {

    private final ServerErrorEnum serverErrorEnum;
    private final String[] args;
    private final LocalDateTime localDateTime;

    public ApplicationRuntimeException(ServerErrorEnum serverError, String ... args) {
        this.serverErrorEnum = serverError;
        this.args = args;
        this.localDateTime = LocalDateTime.now();
    }

    public String getMessage() {
        if (ArrayUtils.isNotEmpty(args)) {
            return serverErrorEnum.getFallBackText().formatted((Object[]) args);
        }

        return serverErrorEnum.getFallBackText();
    }

    public int getCode() {
        return serverErrorEnum.getCode();
    }

    @Override
    public Response getResponse() {
        return Response
                    .status(this.getCode())
                    .entity("{\"message\": \"%s\", \"localDateTime\": \"%s\"}".formatted(this.getMessage(),
                            DateTimeFormatter.ISO_DATE_TIME.format(localDateTime)))
                    .build();
    }
}
