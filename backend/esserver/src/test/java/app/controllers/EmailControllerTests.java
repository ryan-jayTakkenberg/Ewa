package app.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void autowiredSuccessfully() {
        assertNotNull(mockMvc);
    }

    @Test
    void requestPasswordReset_ValidEmail() throws Exception {
        //sends password reset with user@example.com as email
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/request-password-reset")
                .param("email", "user@example.com")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());
    }

    @Test
    void validateResetToken_ValidToken() throws Exception {
        String validToken = "validToken";

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/validate-reset-token")
                .param("token", validToken)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().string("Token is valid."));
    }
}
