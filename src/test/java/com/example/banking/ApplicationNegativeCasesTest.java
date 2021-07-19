package com.example.banking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationNegativeCasesTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAppWithoutAccounts() throws Exception {
        mockMvc.perform(get("/account/99"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAccountCreateNegativeBalance() throws Exception {
        var json = "{\"balance\": -1}";

        mockMvc.perform(post("/account")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAccountCreateEmpty() throws Exception {
        var json = "{}";

        mockMvc.perform(post("/account")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTheSameSenderAndAddressee() throws Exception {
        var transferJson = "{\"sum\": 5, \"from\": 1, \"to\":1}";
        mockMvc.perform(post("/transfer")
                .contentType(APPLICATION_JSON)
                .content(transferJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testIncorrectSum() throws Exception {
        var transferJson = "{\"sum\": 0, \"from\": 1, \"to\":2}";
        mockMvc.perform(post("/transfer")
                .contentType(APPLICATION_JSON)
                .content(transferJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
