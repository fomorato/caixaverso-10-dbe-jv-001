package com.ada.exception;

import com.ada.dto.error.ErrorResponse;
import com.ada.dto.error.ValidationError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        ErrorResponse err = new ErrorResponse();
        err.title = "Constraint Violation";
        err.status = 400;

        for (ConstraintViolation<?> v : exception.getConstraintViolations()) {
            String field = v.getPropertyPath() != null ? v.getPropertyPath().toString() : "";
            err.violations.add(new ValidationError(field, v.getMessage()));
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(err)
                .build();
    }
}
