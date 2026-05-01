package com.ada.test;

import com.ada.client.TaxasRestClient;
import com.ada.client.dto.RateResponse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
public class EmprestimoValidacaoRestAssuredTest extends BaseRestAssuredTest {

    @InjectMock
    @RestClient
    TaxasRestClient taxasRestClient;

    @BeforeEach
    void stubTaxasOk() {
        RateResponse rate = new RateResponse();
        rate.clientId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        rate.taxaJurosMensal = 2;
        when(taxasRestClient.checkEligibility(
                org.mockito.ArgumentMatchers.any(UUID.class),
                org.mockito.ArgumentMatchers.anyString()
        )).thenReturn(rate);

    }

    @Test
    void deveRetornar400QuandoValorTotalMenorQue100() {
        given()
          .auth().preemptive().basic(CLIENTE_USER, CLIENTE_PASS)
          .contentType("application/json")
          .accept("application/json")
          .body("""
            {
              "clienteId": "123e4567-e89b-12d3-a456-426614174000",
              "valorTotal": 50,
              "quantidadeParcelas": 12,
              "tipoAmortizacao": "SAC"
            }
          """)
          .when().post("/emprestimos")
          .then()
            .statusCode(400)
            .body("title", notNullValue())
            .body("status", anyOf(equalTo(400), nullValue()))
            .body("violations", notNullValue());
    }

    @Test
    void deveRetornar400QuandoQuantidadeParcelasMaiorQue480() {
        given()
          .auth().preemptive().basic(CLIENTE_USER, CLIENTE_PASS)
          .contentType("application/json")
          .accept("application/json")
          .body("""
            {
              "clienteId": "123e4567-e89b-12d3-a456-426614174000",
              "valorTotal": 10000,
              "quantidadeParcelas": 481,
              "tipoAmortizacao": "SAC"
            }
          """)
          .when().post("/emprestimos")
          .then()
            .statusCode(400);
    }

    @Test
    void deveRetornar415QuandoContentTypeInvalido() {
        given()
          .auth().preemptive().basic(CLIENTE_USER, CLIENTE_PASS)
          .contentType("text/plain")
          .accept("application/json")
          .body("nao-e-json")
          .when().post("/emprestimos")
          .then()
            .statusCode(anyOf(is(415), is(400)));
    }
}
