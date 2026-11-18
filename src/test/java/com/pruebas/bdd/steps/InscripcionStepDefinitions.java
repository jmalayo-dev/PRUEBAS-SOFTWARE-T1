package com.pruebas.bdd.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InscripcionStepDefinitions {

    @LocalServerPort
    private int port;

    private Response response;
    private String codigoEstudianteActual;
    private Long cursoIdActual;

    private void configurarPuerto() {
        if (RestAssured.port == 8080 || RestAssured.port == 0) {
            RestAssured.port = port;
        }
    }

    @Given("un estudiante nuevo con código {string}")
    public void un_estudiante_nuevo_con_codigo(String codigo) {
        configurarPuerto();
        this.codigoEstudianteActual = codigo;
    }

    @Given("que el estudiante {string} ya está inscrito en el curso {int}")
    public void que_el_estudiante_ya_esta_inscrito_en_el_curso(String codigo, Integer cursoId) {
        configurarPuerto();
        this.codigoEstudianteActual = codigo;
        this.cursoIdActual = cursoId.longValue();
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("codigoEstudiante", codigo);
        requestBody.put("cursoId", cursoId);

        given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/api/inscripciones");
    }

    @When("intenta inscribirse en el curso {int}")
    public void intenta_inscribirse_en_el_curso(Integer cursoId) {
        configurarPuerto();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("codigoEstudiante", codigoEstudianteActual);
        requestBody.put("cursoId", cursoId);

        response = given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/api/inscripciones");
    }

    @When("intenta inscribirse nuevamente en el curso {int}")
    public void intenta_inscribirse_nuevamente_en_el_curso(Integer cursoId) {
        configurarPuerto();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("codigoEstudiante", codigoEstudianteActual);
        requestBody.put("cursoId", cursoId);

        response = given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/api/inscripciones");
    }

    @Then("el sistema responde con el código de estado {int}")
    public void el_sistema_responde_con_el_codigo_de_estado(Integer statusCodeEsperado) {
        response.then().statusCode(statusCodeEsperado);
    }

    @Then("el sistema rechaza la inscripción con un código de error 4xx o 5xx")
    public void el_sistema_rechaza_la_inscripcion_con_un_codigo_de_error_4xx_o_5xx() {
        int statusCode = response.getStatusCode();
        assertTrue(statusCode >= 400 && statusCode < 600, 
            "Se esperaba un código de error 4xx o 5xx, pero se obtuvo: " + statusCode);
    }
}