package com.ada.exception;

import com.ada.dto.error.ErrorResponse;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        int status = exception.getResponse() != null ? exception.getResponse().getStatus() : 500;

        ErrorResponse err = new ErrorResponse();
        err.title = "Erro";
        err.status = status;

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(err)
                .build();
    }
}