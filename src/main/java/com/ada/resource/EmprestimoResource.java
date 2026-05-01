package com.ada.resource;

import com.ada.dto.request.EmprestimoRequest;
import com.ada.mapper.EmprestimoMapper;
import com.ada.service.EmprestimoService;
import com.ada.service.ParcelaService;

import io.quarkus.security.Authenticated;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/emprestimos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class EmprestimoResource {

    @Inject
    EmprestimoService service;

    @Inject
    ParcelaService parcelaService;

    @POST
    public Response criar(@Valid EmprestimoRequest request) {
        var emp = service.criar(request);

        return Response
                .created(URI.create("/emprestimos/" + emp.id))
                .entity(EmprestimoMapper.toResponse(emp))
                .build();
    }

    @GET
    public List<?> listar(@QueryParam("clienteId") UUID clienteId) {
        return service.listar(clienteId)
                .stream()
                .map(EmprestimoMapper::toResponse)
                .toList();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Gerente")
    public Response deletar(@PathParam("id") UUID id) {
        service.deletar(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/parcelas")
    public List<?> listarParcelas(@PathParam("id") UUID emprestimoId) {
        return parcelaService
                .listarPorEmprestimo(emprestimoId)
                .stream()
                .map(EmprestimoMapper::toResponse)
                .toList();
    }
}
