package com.mintos.accounting.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
public class BaseIntegrationTest {
    public static PostgreSQLContainer POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(
                    DockerImageName.parse("postgres:14.4").asCompatibleSubstituteFor("postgres"))
                    .withDatabaseName("accounting")
                    .withUsername("postgres")
                    .withPassword("password");

    @DynamicPropertySource
    static void registerSQLProperties(DynamicPropertyRegistry registry) {
        POSTGRES_SQL_CONTAINER.start();

        registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRES_SQL_CONTAINER::getDriverClassName);
    }

}
