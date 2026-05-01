package com.ada.test;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class SecurityTest extends BaseRestAssuredTest {

    @Test
    void deveRetornar401SemAutenticacao() {
        given()
          .when().get("/emprestimos")
          .then().statusCode(401);
    }

    @Test
    void devePermitir200ComBasicAuth() {
        given()
          .auth().preemptive().basic(CLIENTE_USER, CLIENTE_PASS)
          .when().get("/emprestimos")
          .then().statusCode(200);
    }
}
