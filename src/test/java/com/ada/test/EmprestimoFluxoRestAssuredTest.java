package com.ada.test;

import com.ada.client.TaxasRestClient;
import com.ada.client.dto.RateResponse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.anyOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
public class EmprestimoFluxoRestAssuredTest extends BaseRestAssuredTest {

    @InjectMock
    @RestClient
    TaxasRestClient taxasRestClient;

    @Test
    void fluxoCompleto_SAC_cria_lista_parcelas_paga_deleta() {
        // Arrange: mock da API externa de taxas/elegibilidade
        RateResponse rate = new RateResponse();
        rate.clientId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        rate.taxaJurosMensal = 2;
        when(taxasRestClient.checkEligibility(
                org.mockito.ArgumentMatchers.any(UUID.class),
                org.mockito.ArgumentMatchers.anyString()
        )).thenReturn(rate);


        // 1) POST /emprestimos (criar)
        var createResp = given()
          .auth().preemptive().basic(CLIENTE_USER, CLIENTE_PASS)
          .contentType("application/json")
          .accept("application/json")
          .body("""
            {
              "clienteId": "123e4567-e89b-12d3-a456-426614174000",
              "valorTotal": 10000.00,
              "quantidadeParcelas": 12,
              "tipoAmortizacao": "SAC"
            }
          """)
          .when().post("/emprestimos")
          .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("clienteId", equalTo("123e4567-e89b-12d3-a456-426614174000"))
            .body("taxaJurosMensal", equalTo(2))
            .body("parcelas", hasSize(12))
          .extract();

        String emprestimoId = createResp.path("id").toString();
        String parcelaId = createResp.path("parcelas[0].id").toString();

        // 2) GET /emprestimos (listagem) - deve conter o empréstimo criado
        given()
          .auth().preemptive().basic(CLIENTE_USER, CLIENTE_PASS)
          .when().get("/emprestimos")
          .then()
            .statusCode(200)
            .body("id", hasItem(emprestimoId));

        // 3) GET /emprestimos/{id}/parcelas
        given()
          .auth().preemptive().basic(CLIENTE_USER, CLIENTE_PASS)
          .when().get("/emprestimos/" + emprestimoId + "/parcelas")
          .then()
            .statusCode(200)
            .body("size()", equalTo(12))
            .body("id", hasItem(parcelaId));

        // 4) GET /parcelas/{id} (antes do pagamento)
        given()
          .auth().preemptive().basic(CLIENTE_USER, CLIENTE_PASS)
          .when().get("/parcelas/" + parcelaId)
          .then()
            .statusCode(200)
            .body("id", equalTo(parcelaId))
            .body("status", anyOf(equalTo("PENDENTE"), equalTo("PAGA"), equalTo("CANCELADA")));

        // 5) PATCH /parcelas/{id}/pagamento
        given()
          .auth().preemptive().basic(CLIENTE_USER, CLIENTE_PASS)
          .when().patch("/parcelas/" + parcelaId + "/pagamento")
          .then()
            .statusCode(204);

        // 6) DELETE /emprestimos/{id} deve dar 403 para cliente
        given()
          .auth().preemptive().basic(CLIENTE_USER, CLIENTE_PASS)
          .when().delete("/emprestimos/" + emprestimoId)
          .then()
            .statusCode(403);

        // 7) DELETE /emprestimos/{id} deve dar 204 para gerente
        given()
          .auth().preemptive().basic(GERENTE_USER, GERENTE_PASS)
          .when().delete("/emprestimos/" + emprestimoId)
          .then()
            .statusCode(204);
    }

    @Test
    void deveRetornar400QuandoElegibilidadeNegadaPeloParceiro() {
        // mock: simula parceiro retornando 400
        when(taxasRestClient.checkEligibility(any(UUID.class), anyString()))
            .thenThrow(new WebApplicationException("inelegível", Response.Status.BAD_REQUEST));

        given()
          .auth().preemptive().basic(CLIENTE_USER, CLIENTE_PASS)
          .contentType("application/json")
          .accept("application/json")
          .body("""
            {
              "clienteId": "123e4567-e89b-12d3-a456-426614174000",
              "valorTotal": 10000.00,
              "quantidadeParcelas": 12,
              "tipoAmortizacao": "SAC"
            }
          """)
          .when().post("/emprestimos")
          .then()
            .statusCode(400);
    }
}
