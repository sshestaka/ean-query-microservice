package mercadona.ean.code.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mercadona.ean.code.api.dto.product.CreateProductRequestDto;
import mercadona.ean.code.api.dto.product.ProductDto;
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
public class ProductControllerTest {
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
                    new ClassPathResource("database/products/remove-all-products.sql")
            );
        }
    }

    @Test
    @DisplayName("Create a new Product")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/products/remove-all-products.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void createProduct_ValidRequestDto_Success() throws Exception {
        CreateProductRequestDto requestDto = getTestProductDto();
        ProductDto expected = getProduct1Dto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/products")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        ProductDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ProductDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Get all products")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(
            scripts = "classpath:database/products/add-three-default-products.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/products/remove-all-products.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getAll_GivenProductsInCatalog_ShouldReturnAllProducts() throws Exception {
        List<ProductDto> expected = new ArrayList<>();
        expected.add(getProduct1Dto());
        expected.add(getProduct2Dto());
        expected.add(getProduct3Dto());
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        ProductDto[] actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                ProductDto[].class
        );
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @DisplayName("Find product by id")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(
            scripts = "classpath:database/products/add-three-default-products.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/products/remove-all-products.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findById_GivenExistingProductId_ShouldReturnProduct() throws Exception {
        ProductDto expected1 = getProduct1Dto();
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        ProductDto actual1 = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ProductDto.class
        );
        Assertions.assertNotNull(actual1);
        EqualsBuilder.reflectionEquals(expected1, actual1);
    }

    @Test
    @DisplayName("Find product by Code")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(
            scripts = "classpath:database/products/add-three-default-products.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/products/remove-all-products.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findByCode_GivenExistingProductCode_ShouldReturnProduct() throws Exception {
        ProductDto expected1 = getProduct1Dto();
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/products/code/45905")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        ProductDto actual1 = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ProductDto.class
        );
        Assertions.assertNotNull(actual1);
        EqualsBuilder.reflectionEquals(expected1, actual1);
    }

    @Test
    @DisplayName("Delete product by id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/products/add-three-default-products.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/products/remove-all-products.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void delete_DeleteExistedProduct_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        delete("/api/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(actual.isEmpty());
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

    private CreateProductRequestDto getTestProductDto() {
        return new CreateProductRequestDto()
                .setCode(12345)
                .setTitle("Title-12345")
                .setCategory("Category00012345")
                .setPackaging("Package012345")
                .setRating("Rating-1");
    }
}
