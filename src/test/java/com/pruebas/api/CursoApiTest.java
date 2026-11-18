package com.pruebas.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CursoApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void testConsultarTodosLosCursos() {
        given()
            .when()
                .get("/api/cursos")
            .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    void testConsultarCursoPorId1() {
        given()
            .when()
                .get("/api/cursos/1")
            .then()
                .statusCode(200)
                .body("nombre", equalTo("Pruebas de Software"))
                .body("precio", greaterThan(0.0f));
    }
}