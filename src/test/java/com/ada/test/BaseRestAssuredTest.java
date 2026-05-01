package com.ada.test;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseRestAssuredTest {

    protected static final String CLIENTE_USER = "cliente";
    protected static final String CLIENTE_PASS = "senha123";
    protected static final String GERENTE_USER = "gerente";
    protected static final String GERENTE_PASS = "senha123";

    @BeforeAll
    static void setup() {
        RestAssured.basePath = "/";
    }
}
