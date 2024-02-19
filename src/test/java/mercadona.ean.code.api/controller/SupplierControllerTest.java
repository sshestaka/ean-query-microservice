package mercadona.ean.code.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mercadona.ean.code.api.dto.supplier.CreateSupplierRequestDto;
import mercadona.ean.code.api.dto.supplier.SupplierDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SupplierControllerTest {
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
                    new ClassPathResource("database/suppliers/remove-all-suppliers.sql")
            );
        }
    }

    @Test
    @DisplayName("Create a new Supplier")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/suppliers/remove-all-suppliers.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void createSupplier_ValidRequestDto_Success() throws Exception {
        CreateSupplierRequestDto requestDto = getTestSupplierDto();
        SupplierDto expected = getSupplier1Dto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/suppliers")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        SupplierDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                SupplierDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Get all suppliers")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(
            scripts = "classpath:database/suppliers/add-three-default-suppliers.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/suppliers/remove-all-suppliers.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getAll_GivenSuppliersInCatalog_ShouldReturnAllSuppliers() throws Exception {
        List<SupplierDto> expected = new ArrayList<>();
        expected.add(getSupplier1Dto());
        expected.add(getSupplier2Dto());
        expected.add(getSupplier3Dto());
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/suppliers")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        SupplierDto[] actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                SupplierDto[].class
        );
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @DisplayName("Find supplier by id")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(
            scripts = "classpath:database/suppliers/add-three-default-suppliers.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/suppliers/remove-all-suppliers.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findById_GivenExistingSupplierId_ShouldReturnSupplier() throws Exception {
        SupplierDto expected1 = getSupplier1Dto();
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/suppliers/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        SupplierDto actual1 = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                SupplierDto.class
        );
        Assertions.assertNotNull(actual1);
        EqualsBuilder.reflectionEquals(expected1, actual1);
    }

    @Test
    @DisplayName("Find supplier by Code")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(
            scripts = "classpath:database/suppliers/add-three-default-suppliers.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/suppliers/remove-all-suppliers.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findByCode_GivenExistingSupplierCode_ShouldReturnSupplier() throws Exception {
        SupplierDto expected1 = getSupplier1Dto();
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/suppliers/code/8437008")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        SupplierDto actual1 = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                SupplierDto.class
        );
        Assertions.assertNotNull(actual1);
        EqualsBuilder.reflectionEquals(expected1, actual1);
    }

    @Test
    @DisplayName("Delete supplier by id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/suppliers/add-three-default-suppliers.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/suppliers/remove-all-suppliers.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void delete_DeleteExistedSupplier_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        delete("/api/suppliers/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(actual.isEmpty());
    }

    private SupplierDto getSupplier1Dto() {
        return new SupplierDto()
                .setId(1L)
                .setCode(8437008)
                .setName("Supplier-1")
                .setEban("EBAN:0001")
                .setAddress("ADDRESS-01")
                .setPhoneNumber("+34111222333")
                .setDescription("Description-1");

    }

    private SupplierDto getSupplier2Dto() {
        return new SupplierDto()
                .setId(2L)
                .setCode(8480000)
                .setName("Supplier-2")
                .setEban("EBAN:0002")
                .setAddress("ADDRESS-02")
                .setPhoneNumber("+34444555666")
                .setDescription("Description-2");

    }

    private SupplierDto getSupplier3Dto() {
        return new SupplierDto()
                .setId(3L)
                .setCode(8484884)
                .setName("Supplier-3")
                .setEban("EBAN:0003")
                .setAddress("ADDRESS-03")
                .setPhoneNumber("+34777888999")
                .setDescription("Description-3");

    }

    private CreateSupplierRequestDto getTestSupplierDto() {
        return new CreateSupplierRequestDto()
                .setCode(12345)
                .setName("Supplier-12345")
                .setEban("EBAN:00012345")
                .setAddress("ADDRESS-012345")
                .setPhoneNumber("+34123456789")
                .setDescription("Description-Test");
    }
}
