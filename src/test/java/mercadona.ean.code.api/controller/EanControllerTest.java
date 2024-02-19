package mercadona.ean.code.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mercadona.ean.code.api.dto.ean.EanDTO;
import mercadona.ean.code.api.dto.product.ProductDto;
import mercadona.ean.code.api.dto.supplier.SupplierDto;
import mercadona.ean.code.api.model.Destination;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EanControllerTest {
    private static final int DESTINATION_1 = 1;
    private static final int DESTINATION_2 = 6;
    private static final int DESTINATION_3 = 9;

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
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/products/add-three-default-products.sql"
                    )
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/suppliers/add-three-default-suppliers.sql"
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
                    new ClassPathResource("database/remove-all-data-before-tests/remove-all-data-before-tests.sql")
            );
        }
    }

    @Test
    @DisplayName("Get EAN details 1")
    @WithMockUser(username = "user", roles = {"USER"})
    void getEanInfo1_GivenExistingEanCode_ShouldReturnAllEanDetails() throws Exception {
        EanDTO expected = getEan1Dto();
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/eans/8437008459051")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        EanDTO actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                EanDTO.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get EAN details 2")
    @WithMockUser(username = "user", roles = {"USER"})
    void getEanInfo2_GivenExistingEanCode_ShouldReturnAllEanDetails() throws Exception {
        EanDTO expected = getEan2Dto();
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/eans/8480000160076")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        EanDTO actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                EanDTO.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get EAN details 3")
    @WithMockUser(username = "user", roles = {"USER"})
    void getEanInfo3_GivenExistingEanCode_ShouldReturnAllEanDetails() throws Exception {
        EanDTO expected = getEan3Dto();
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/eans/8484884123459")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        EanDTO actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                EanDTO.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    private EanDTO getEan1Dto() {
        return new EanDTO()
                .setProduct(getProduct1Dto())
                .setSupplier(getSupplier1Dto())
                .setDestination(getDestination1());
    }

    private EanDTO getEan2Dto() {
        return new EanDTO()
                .setProduct(getProduct2Dto())
                .setSupplier(getSupplier2Dto())
                .setDestination(getDestination2());
    }

    private EanDTO getEan3Dto() {
        return new EanDTO()
                .setProduct(getProduct3Dto())
                .setSupplier(getSupplier3Dto())
                .setDestination(getDestination3());
    }

    private Destination getDestination1() {
        return Destination.fromCode(DESTINATION_1);
    }

    private Destination getDestination2() {
        return Destination.fromCode(DESTINATION_2);
    }

    private Destination getDestination3() {
        return Destination.fromCode(DESTINATION_3);
    }

    private ProductDto getProduct1Dto() {
        return new ProductDto()
                .setId(1L)
                .setCode(45905)
                .setTitle("Product-1")
                .setCategory("Category-1")
                .setPackaging("Package-01")
                .setRating("Rating-1");

    }

    private ProductDto getProduct2Dto() {
        return new ProductDto()
                .setId(2L)
                .setCode(16007)
                .setTitle("Product-2")
                .setCategory("Category-2")
                .setPackaging("Package-02")
                .setRating("Rating-2");

    }

    private ProductDto getProduct3Dto() {
        return new ProductDto()
                .setId(3L)
                .setCode(12345)
                .setTitle("Product-3")
                .setCategory("Category-3")
                .setPackaging("Package-03")
                .setRating("Rating-3");

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
}
