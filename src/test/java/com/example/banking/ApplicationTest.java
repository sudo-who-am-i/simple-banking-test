package com.example.banking;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testAccountCreate() throws Exception {
        var json = "{\"balance\": 10}";

        mockMvc.perform(post("/account")
                .contentType(APPLICATION_JSON)
                .content(json)
        );
        mockMvc.perform(post("/account")
                .contentType(APPLICATION_JSON)
                .content(json)
        );
        var res = "{\"id\":1,\"balance\":10.00,\"transfers\":[]}";
        mockMvc.perform(get("/account/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(res));

    }

    @Test
    @Order(2)
    public void testAppTransfer() throws Exception {
        var transferJson = "{\"sum\": 5, \"from\": 1, \"to\":2}";
        mockMvc.perform(post("/transfer")
                .contentType(APPLICATION_JSON)
                .content(transferJson)
        );

        mockMvc.perform(get("/account/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(5.0))
                .andExpect(jsonPath("$.transfers[0].from").value(1))
                .andExpect(jsonPath("$.transfers[0].to").value(2));
    }

    @Test
    @Order(3)
    public void testTheSameSenderAndAddressee() throws Exception {
        var transferJson = "{\"sum\": 5, \"from\": 1, \"to\":1}";
        mockMvc.perform(post("/transfer")
                .contentType(APPLICATION_JSON)
                .content(transferJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
