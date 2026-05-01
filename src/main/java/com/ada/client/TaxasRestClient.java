package com.ada.client;

import com.ada.client.dto.RateResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.UUID;

@Path("/clientes/{clienteId}/taxas/{tipoAmortizacao}/elegibilidade")
@RegisterRestClient(configKey = "taxas-api")
@Produces(MediaType.APPLICATION_JSON)
public interface TaxasRestClient {

    @GET
    RateResponse checkEligibility(@PathParam("clienteId") UUID clienteId,
                                  @PathParam("tipoAmortizacao") String tipoAmortizacao);
}
