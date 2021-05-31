package com.juliamartyn.goldenbook.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application-test.properties"})
@ActiveProfiles("test")
public class BookControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails("admin")
    public void createWithRoleAdmin() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "image.jpg", "application/octet-stream", new byte[0]);
        MockPart book = new MockPart("book", "{\"title\":\"Boook\", \"author\":\"Author Author\",\"category\":\"Crime\",\"description\":\"description\",\"price\":\"100\",\"quantity\":\"1\"}".getBytes(UTF_8));
        mvc.perform(multipart("/api/books")
                .part(book).file(file))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails("seller")
    public void createWithRoleSeller() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "image.jpg", "application/octet-stream", new byte[0]);
        MockPart book = new MockPart("book", "{\"title\":\"Boook\", \"author\":\"Author Author\",\"category\":\"Crime\",\"description\":\"description\",\"price\":\"100\",\"quantity\":\"1\"}".getBytes(UTF_8));
        mvc.perform(multipart("/api/books")
                .part(book).file(file))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails("customer")
    public void createWithRoleCustomer() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "image.jpg", "application/octet-stream", new byte[0]);
        MockPart book = new MockPart("book", "{\"title\":\"Boook\", \"author\":\"Author Author\",\"category\":\"Crime\",\"description\":\"description\",\"price\":\"100\",\"quantity\":\"1\"}".getBytes(UTF_8));
        mvc.perform(multipart("/api/books")
                .part(book).file(file))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("admin")
    public void findAllWithRoleAdmin() throws Exception {
        mvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void findAllWithRoleSeller() throws Exception {
        mvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void findAllWithRoleCustomer() throws Exception {
        mvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void findAllWithoutUser() throws Exception {
        mvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithUserDetails("admin")
    public void findByIdWithRoleAdmin() throws Exception {
        mvc.perform(get("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void findByIdWithRoleSeller() throws Exception {
        mvc.perform(get("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void findByIdWithRoleCustomer() throws Exception {
        mvc.perform(get("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("admin")
    public void pageableBooksWithRoleAdmin() throws Exception {
        mvc.perform(get("/api/books/page/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("seller")
    public void pageableBooksWithRoleSeller() throws Exception {
        mvc.perform(get("/api/books/page/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void pageableBooksWithRoleCustomer() throws Exception {
        mvc.perform(get("/api/books/page/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @WithUserDetails("admin")
    public void updatePricedWithRoleAdmin() throws Exception {
        mvc.perform(patch("/api/books/1/price")
                .content("{\"price\":\"100\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void updatePriceWithRoleSeller() throws Exception {
        mvc.perform(patch("/api/books/1/price")
                .content("{\"price\":\"100\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void updatePriceWithRoleCustomer() throws Exception {
        mvc.perform(patch("/api/books/3/price")
                .content("{\"price\":\"100\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("admin")
    public void updateQuantityWithRoleAdmin() throws Exception {
        mvc.perform(patch("/api/books/1/quantity")
                .content("{\"quantity\":\"1\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void updateQuantityWithRoleSeller() throws Exception {
        mvc.perform(patch("/api/books/4/quantity")
                .content("{\"quantity\":\"1\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void updateQuantityWithRoleCustomer() throws Exception {
        mvc.perform(patch("/api/books/1/quantity")
                .content("{\"quantity\":\"1\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("admin")
    public void deleteWithRoleAdmin() throws Exception {
        mvc.perform(delete("/api/books/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void deleteWithRoleSeller() throws Exception {
        mvc.perform(delete("/api/books/6")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void deleteWithRoleCustomer() throws Exception {
        mvc.perform(delete("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("admin")
    public void applyDiscountWithRoleAdmin() throws Exception {
        mvc.perform(post("/api/books/apply-discount")
                .content("{\"discount\":\"2\", \"dueDate\": \"2021-06-01\", \"booksId\": [3]}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void applyDiscountWithRoleSeller() throws Exception {
        mvc.perform(post("/api/books/apply-discount")
                .content("{\"discount\":\"2\", \"dueDate\": \"2021-06-01\", \"booksId\": [1]}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void applyDiscountWithRoleCustomer() throws Exception {
        mvc.perform(post("/api/books/apply-discount")
                .content("{\"discount\":\"2\", \"dueDate\": \"2021-06-01\", \"booksId\": [2]}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("admin")
    public void addEBookWithRoleAdmin() throws Exception {
        MockMultipartFile file = new MockMultipartFile("eBookFile", "book.txt", "application/octet-stream", new byte[0]);
        MockPart book = new MockPart("eBook", "{\"price\":\"10\"}".getBytes(UTF_8));
        mvc.perform(multipart("/api/books/1/add-e-book")
                .part(book).file(file))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void addEBookWithRoleSeller() throws Exception {
        MockMultipartFile file = new MockMultipartFile("eBookFile", "book.txt", "application/octet-stream", new byte[0]);
        MockPart book = new MockPart("eBook", "{\"price\":\"10\"}".getBytes(UTF_8));
        mvc.perform(multipart("/api/books/1/add-e-book")
                .part(book).file(file))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void addEBookWithRoleCustomer() throws Exception {
        MockMultipartFile file = new MockMultipartFile("eBookFile", "book.txt", "application/octet-stream", new byte[0]);
        MockPart book = new MockPart("eBook", "{\"price\":\"10\"}".getBytes(UTF_8));
        mvc.perform(multipart("/api/books/1/add-e-book")
                .part(book).file(file))
                .andExpect(status().isForbidden());
    }
}
