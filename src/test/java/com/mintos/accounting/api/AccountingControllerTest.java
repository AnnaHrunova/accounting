package com.mintos.accounting.api;

import com.mintos.accounting.config.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Deprecated(since = "Replaced by rest-assured")
class AccountingControllerTest extends BaseIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void createClient_success() throws Exception {
        var requestJson = """
                {
                  "firstName": "Jane",
                  "lastName": "Doe"
                }
                """;

        var requestBuilder = post("/accounting/v1/clients")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());
    }
}
