package com.ada.client;

import com.ada.dto.ViaCepResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/ws")
@RegisterRestClient(configKey = "via-cep")
public interface ViaCepRestClient {

    @GET
    @Path("/{cep}/json")
    ViaCepResponse getByCep(@PathParam("cep") String cep);
}