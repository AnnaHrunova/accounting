package com.mintos.accounting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Objects;

@SpringBootTest(    classes = IntegrationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseRestAssuredTest {
    @LocalServerPort
    protected Integer localPort;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        String baseHost = System.getProperty("server.host");
        RestAssured.baseURI = Objects.requireNonNullElse(baseHost, "http://localhost");

        String port = System.getProperty("server.port");
        if (port == null) {
            RestAssured.port = localPort;
        } else {
            RestAssured.port = Integer.parseInt(port);
        }

        RestAssured.config =
                RestAssuredConfig.config()
                        .objectMapperConfig(
                                new ObjectMapperConfig()
                                        .jackson2ObjectMapperFactory(
                                                (cls, charset) -> objectMapper));
    }

}
