package com.ada.exception;

import com.ada.dto.error.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TaxasElegibilidadeExceptionMapper implements ExceptionMapper<TaxasElegibilidadeException> {

    @Override
    public Response toResponse(TaxasElegibilidadeException exception) {
        ErrorResponse err = new ErrorResponse();
        err.title = "Risco financeiro negado ou falha na comunicação";
        err.status = 400;
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(err)
                .build();
    }
}
