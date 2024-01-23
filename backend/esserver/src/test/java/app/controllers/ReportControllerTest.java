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
        // Arrange: Set up the request builder with the appropriate endpoint and authorization header
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/reports")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        // Act: Perform the request using MockMvc and obtain the response actions
        ResultActions response = mockMvc.perform(builder);

        // Assert: Validate that the response meets the expected criteria
        response.andExpectAll(
                status().isOk(),           // Assert HTTP status code is 200 (OK)
                jsonPath("$").isArray()    // Assert the response body is a JSON array
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
        // Arrange: Create a sample report object and convert it to JSON
        Report report = new Report(0, "Test report body", "23/12/2023", 1, "Tobi", 2);
        ObjectMapper objectMapper = new ObjectMapper();
        String reportJson = objectMapper.writeValueAsString(report);

        // Act: Build and perform a POST request to create a report
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reportJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);

        // Assert: Validate the response for successful report creation
        response.andExpectAll(
                status().isCreated(),                             // Assert HTTP status code is 201 (Created)
                jsonPath("$.body").value(report.getBody()),      // Assert the response body matches the input
                jsonPath("$.date").value(report.getDate()),       // Assert the response date matches the input
                jsonPath("$.senderId").value(report.getSenderId()),    // Assert senderId in the response
                jsonPath("$.senderName").value(report.getSenderName()), // Assert senderName in the response
                jsonPath("$.receiverId").value(report.getReceiverId())  // Assert receiverId in the response
        );

        // Additional Assertions: Validate the response JSON and report ID
        String responseJson = response.andReturn().getResponse().getContentAsString();
        report = objectMapper.readValue(responseJson, Report.class);

        assertNotNull(report);                   // Assert that the returned report object is not null
        assertTrue(report.getId() > 0);          // Assert that the report ID is greater than 0
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
        // Arrange: Create a new report object and convert it to JSON
        Report newReport = new Report(2, "Reminder: Team meeting tomorrow.", "23/12/2023", 1, "admin", 3);
        ObjectMapper objectMapper = new ObjectMapper();
        String reportJson = objectMapper.writeValueAsString(newReport);

        // Act: Build and perform a POST request to post a new report
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/reports", newReport.getId())   // Specify the endpoint with the report ID
                .contentType(MediaType.APPLICATION_JSON)
                .content(reportJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);

        // Assert: Validate the response for successful report posting
        response.andExpectAll(
                status().isCreated(),                             // Assert HTTP status code is 201 (Created)
                jsonPath("$.id").value(greaterThan(0)),           // Ensure the ID is positive
                jsonPath("$.body").value(newReport.getBody()),   // Assert the response body matches the input
                jsonPath("$.date").value(newReport.getDate()),    // Assert the response date matches the input
                jsonPath("$.senderId").value(newReport.getSenderId()),    // Assert senderId in the response
                jsonPath("$.senderName").value(newReport.getSenderName()), // Assert senderName in the response
                jsonPath("$.receiverId").value(newReport.getReceiverId())  // Assert receiverId in the response
        );

        // Additional Assertions: Verify that the posted report is saved to the repository
        String responseJson = response.andReturn().getResponse().getContentAsString();
        Report postedReport = objectMapper.readValue(responseJson, Report.class);
        assertNotNull(reportRepo.findById(postedReport.getId())); // Verify that the report is saved to the repository
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
        // Arrange: Create and save a report to be deleted
        Report reportToDelete = new Report(2, "Reminder: Team meeting tomorrow.", "23/12/2023", 1, "admin", 3);
        reportRepo.save(reportToDelete);

        // Act: Build and perform a DELETE request to delete the specified report
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/reports/{id}", reportToDelete.getId())   // Specify the endpoint with the report ID
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);

        // Assert: Validate the response for successful report deletion
        response.andExpectAll(
                status().isOk(),                                                          // Assert HTTP status code is 200 (OK)
                jsonPath("$.id").value(reportToDelete.getId()),                 // Assert the response ID matches the input
                jsonPath("$.body").value(reportToDelete.getBody()),             // Assert the response body matches the input
                jsonPath("$.date").value(reportToDelete.getDate()),             // Assert the response date matches the input
                jsonPath("$.senderId").value(reportToDelete.getSenderId()),     // Assert senderId in the response
                jsonPath("$.senderName").value(reportToDelete.getSenderName()), // Assert senderName in the response
                jsonPath("$.receiverId").value(reportToDelete.getReceiverId())  // Assert receiverId in the response
        );

        // Additional Assertions: Verify that the report is deleted from the repository
        assertNull(reportRepo.findById(reportToDelete.getId())); // Ensure the report is deleted from the repository
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
        // Arrange: Create an empty report (invalid data)
        Report invalidReport = new Report();

        ObjectMapper objectMapper = new ObjectMapper();
        String invalidReportJson = objectMapper.writeValueAsString(invalidReport);

        // Act: Build and perform a POST request with invalid data
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidReportJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);

        // Assert: Validate that the response returns a bad request status
        response.andExpect(status().isBadRequest());
    }

}