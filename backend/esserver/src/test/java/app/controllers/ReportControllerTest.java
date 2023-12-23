package app.controllers;

import app.enums.PermissionLevel;
import app.jwt.JWTConfig;
import app.jwt.JWToken;
import app.models.Report;
import app.repositories.ReportJPARepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Locale;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReportJPARepository reportRepo;

    private static String token;

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);

        JWTConfig JwtConfig = new JWTConfig();
        JWToken jwt = new JWToken(-1, PermissionLevel.ADMIN, "127.0.0.1", JwtConfig.getExpiration());
        token = jwt.encode(JwtConfig.getIssuer(), JwtConfig.getPassphrase());

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @BeforeEach
    void autowiredSuccessfully() {
        assertNotNull(mockMvc);
    }

    /**
     * Test the behavior of retrieving a list of reports.
     * <p>
     * The test sends a GET request to the "/reports" endpoint, expecting a successful response
     * (HTTP 200 OK). Additionally, it validates that the response body represents a JSON array of reports.
     * </p>
     * <p>
     * Test Steps:
     * 1. Send a GET request to the "/reports" endpoint.
     * 2. Expect a successful response with HTTP 200 OK.
     * 3. Validate that the response body represents a JSON array of reports.
     * </p>
     *
     * @throws Exception if an error occurs during test execution.
     */
    @Test
    void getReports() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/reports")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").isArray()
        );
    }

    /**
     * Test the behavior of creating a new report.
     * <p>
     * The test creates a new report, serializes it to JSON, sends a POST request to the "/reports" endpoint,
     * and expects a successful response (HTTP 201 Created). Additionally, it validates that the response body
     * contains the details of the created report, including the correct ID. Finally, it checks that the created report
     * is successfully retrieved from the response and has a positive ID.
     * </p>
     * <p>
     * Test Steps:
     * 1. Create a new report to be created.
     * 2. Serialize the report to JSON.
     * 3. Send a POST request to the "/reports" endpoint with the new report data.
     * 4. Expect a successful response with HTTP 201 Created.
     * 5. Validate that the response body contains the details of the created report.
     * 6. Retrieve the created report from the response and ensure it has a positive ID.
     * </p>
     *
     * @throws Exception if an error occurs during test execution.
     */
    @Test
    void createReport() throws Exception {
        Report report = new Report(1, "Hello Jason, please notice that at the end of this week project 5 is due.", "23/12/2023", 1, "admin", 2);

        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(report);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isCreated(),
                jsonPath("$.id").value(report.getId()),
                jsonPath("$.body").value(report.getBody()),
                jsonPath("$.date").value(report.getDate()),
                jsonPath("$.senderId").value(report.getSenderId()),
                jsonPath("$.senderName").value(report.getSenderName()),
                jsonPath("$.receiverId").value(report.getReceiverId())
        );

        String responseJson = response.andReturn().getResponse().getContentAsString();
        report = objectMapper.readValue(responseJson, Report.class);

        assertNotNull(report);
        assertTrue(report.getId() > 0); // Report ID must be higher than 0
    }

    /**
     * Test the behavior of posting a new report.
     * <p>
     * The test creates a new report, serializes it to JSON, sends a POST request to the "/reports" endpoint,
     * and expects a successful response (HTTP 201 Created). Additionally, it validates that the response body
     * contains the details of the posted report, including a positive ID. Finally, it checks that the posted report
     * is successfully saved to the repository.
     * </p>
     * <p>
     * Test Steps:
     * 1. Create a new report to be posted.
     * 2. Serialize the report to JSON.
     * 3. Send a POST request to the "/reports" endpoint with the new report data.
     * 4. Expect a successful response with HTTP 201 Created.
     * 5. Validate that the response body contains the details of the posted report.
     * 6. Ensure that the posted report is saved to the repository.
     * </p>
     *
     * @throws Exception if an error occurs during test execution.
     */
    @Test
    void postReport() throws Exception {
        Report newReport = new Report(2, "Reminder: Team meeting tomorrow.", "23/12/2023", 1, "admin", 3);

        ObjectMapper objectMapper = new ObjectMapper();
        String reportJson = objectMapper.writeValueAsString(newReport);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/reports", newReport.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(reportJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isCreated(),
                jsonPath("$.id").value(greaterThan(0)), // Ensure the ID is positive
                jsonPath("$.body").value(newReport.getBody()),
                jsonPath("$.date").value(newReport.getDate()),
                jsonPath("$.senderId").value(newReport.getSenderId()),
                jsonPath("$.senderName").value(newReport.getSenderName()),
                jsonPath("$.receiverId").value(newReport.getReceiverId())
        );

        // Extract the posted report from the response
        String responseJson = response.andReturn().getResponse().getContentAsString();
        Report postedReport = objectMapper.readValue(responseJson, Report.class);

        // Verify that the report is saved to the repository
        assertNotNull(reportRepo.findById(postedReport.getId()));
    }

    /**
     * Test the behavior of deleting a report.
     * <p>
     * The test creates a report, saves it to the repository, sends a DELETE request to the "/reports/{id}" endpoint,
     * and expects a successful response (HTTP 200 OK). Additionally, it validates that the response body matches the
     * details of the deleted report. Finally, it checks that the report is successfully deleted from the repository.
     * </p>
     * <p>
     * Test Steps:
     * 1. Create a report to be deleted.
     * 2. Save the report to the repository.
     * 3. Send a DELETE request to the "/reports/{id}" endpoint.
     * 4. Expect a successful response with HTTP 200 OK.
     * 5. Validate that the response body matches the details of the deleted report.
     * 6. Ensure that the report is deleted from the repository.
     * </p>
     *
     * @throws Exception if an error occurs during test execution.
     */
    @Test
    void deleteReport() throws Exception {
        Report reportToDelete = new Report(2, "Reminder: Team meeting tomorrow.", "23/12/2023", 1, "admin", 3);
        reportRepo.save(reportToDelete);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/reports/{id}", reportToDelete.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(reportToDelete.getId()),
                jsonPath("$.body").value(reportToDelete.getBody()),
                jsonPath("$.date").value(reportToDelete.getDate()),
                jsonPath("$.senderId").value(reportToDelete.getSenderId()),
                jsonPath("$.senderName").value(reportToDelete.getSenderName()),
                jsonPath("$.receiverId").value(reportToDelete.getReceiverId())
        );

        // Ensure the report is deleted from the repository
        assertNull(reportRepo.findById(reportToDelete.getId()));
    }

    /**
     * Test the behavior of posting a report with invalid data, expecting a Bad Request response.
     * <p>
     * The test creates an empty report (invalid data), serializes it to JSON, and sends a POST request to the "/reports" endpoint.
     * The expectation is that the response status should be Bad Request (HTTP 400).
     * </p>
     * <p>
     * Test Steps:
     * 1. Create an empty report (invalid data).
     * 2. Serialize the report to JSON.
     * 3. Send a POST request to the "/reports" endpoint with the invalid report data.
     * 4. Expect the response status to be Bad Request.
     * </p>
     *
     * @throws Exception if an error occurs during test execution.
     */
    @Test
    void postReportWithInvalidData() throws Exception {
        // Create an empty report (invalid data)
        Report invalidReport = new Report();

        ObjectMapper objectMapper = new ObjectMapper();
        String invalidReportJson = objectMapper.writeValueAsString(invalidReport);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidReportJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isBadRequest());
    }

}