package com.ada.resource;

import com.ada.mapper.EmprestimoMapper;
import com.ada.service.ParcelaService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/parcelas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class ParcelaResource {

    @Inject
    ParcelaService service;

    @GET
    @Path("/{id}")
    public Response buscarParcela(@PathParam("id") UUID parcelaId) {
        var p = service.buscar(parcelaId);
        if (p == null) {
            throw new NotFoundException();
        }
        return Response.ok(EmprestimoMapper.toResponse(p)).build();
    }

    @PATCH
    @Path("/{id}/pagamento")
    public Response baixar(@PathParam("id") UUID parcelaId) {
        var p = service.buscar(parcelaId);
        if (p == null) {
            throw new NotFoundException();
        }
        service.baixarPagamento(parcelaId);
        return Response.noContent().build();
    }
}
