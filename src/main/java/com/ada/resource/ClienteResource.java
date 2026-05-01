package com.ada.resource;

import com.ada.dto.request.ClienteRequest;
import com.ada.dto.response.ClienteResponse;
import com.ada.service.ClienteService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteResource {

    private final ClienteService service;

    public ClienteResource(ClienteService service) {
        this.service = service;
    }

    @POST
    public Response criar(@Valid ClienteRequest req) {
        var cliente = service.criar(req);
        return Response.status(Response.Status.CREATED)
                .entity(ClienteResponse.from(cliente))
                .build();
    }

    @GET
    public List<ClienteResponse> listar() {
        return service.listar().stream().map(ClienteResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    public ClienteResponse buscarPorId(@PathParam("id") UUID id) {
        return ClienteResponse.from(service.buscarPorId(id));
    }

    @GET
    @Path("/documento/{doc}")
    public ClienteResponse buscarPorDocumento(@PathParam("doc") String doc) {
        return ClienteResponse.from(service.buscarPorDocumento(doc));
    }

    @PUT
    @Path("/{id}")
    public ClienteResponse atualizar(@PathParam("id") UUID id, @Valid ClienteRequest req) {
        return ClienteResponse.from(service.atualizar(id, req));
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") UUID id) {
        service.remover(id);
        return Response.noContent().build();
    }
}