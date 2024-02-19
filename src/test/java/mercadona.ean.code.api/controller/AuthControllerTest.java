package mercadona.ean.code.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mercadona.ean.code.api.dto.user.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/remove-all-data-before-tests/remove-all-data-before-tests.sql"
                    )
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/users/remove-all-users.sql")
            );
        }
    }

    @Test
    @DisplayName("User login test")
    @Sql(
            scripts = "classpath:database/users/add-two-users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/users/remove-all-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void userLoginTest_givenExistedUser_ShouldReturnSuccess() throws Exception {
        UserLoginRequestDto requestDto = getUserLoginRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/auth/login")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        String token = extractTokenFromJson(responseBody);
        Assertions.assertNotNull(token);
    }

    private String extractTokenFromJson(String responseBody) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(responseBody);
        return rootNode.get("token").textValue();
    }

    @Test
    @DisplayName("Admin login test")
    @Sql(
            scripts = "classpath:database/users/add-two-users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/users/remove-all-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void adminLoginTest_givenExistedUser_ShouldReturnSuccess() throws Exception {
        UserLoginRequestDto requestDto = getAdminLoginRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/auth/login")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        UserLoginResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                UserLoginResponseDto.class
        );
        Assertions.assertNotNull(actual);
    }

    @Test
    @DisplayName("User register test")
    @Sql(
            scripts = "classpath:database/users/remove-all-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void userRegisterTest_givenValidRequestForRegisterNewUser_ShouldReturnSuccess()
            throws Exception {
        UserRegistrationRequestDto requestDto = getUserRegistrationRequestDto();
        UserResponseDto expected = getUserResponseDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/auth/registration")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        UserResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                UserResponseDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Incorrect user email register test")
    @Sql(
            scripts = "classpath:database/users/remove-all-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void incorrectUserRegisterTest_givenIncorrectEmailForRegisterNewUser_ShouldReturnException()
            throws Exception {
        UserRegistrationRequestDto requestDto = getIncorrectUserRegistrationRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/auth/register")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private UserLoginRequestDto getUserLoginRequestDto() {
        return new UserLoginRequestDto("user@gmail.com",
                "1234");
    }

    private UserLoginRequestDto getAdminLoginRequestDto() {
        return new UserLoginRequestDto("admin@gmail.com",
                "1234");
    }

    private UserRegistrationRequestDto getUserRegistrationRequestDto() {
        return new UserRegistrationRequestDto()
                .setEmail("user1@gmail.com")
                .setPassword("1234")
                .setRepeatPassword("1234")
                .setFirstName("user")
                .setLastName("user")
                .setDepartment("Department");
    }

    private UserRegistrationRequestDto getIncorrectUserRegistrationRequestDto() {
        return new UserRegistrationRequestDto()
                .setEmail("email")
                .setPassword("newpassword")
                .setRepeatPassword("newpassword")
                .setFirstName("new user")
                .setLastName("new user")
                .setDepartment("new Department");
    }

    private UserResponseDto getUserResponseDto() {
        return new UserResponseDto()
                .setId(1L)
                .setEmail("user1@gmail.com")
                .setFirstName("user")
                .setLastName("user")
                .setDepartment("Department");
    }
}
