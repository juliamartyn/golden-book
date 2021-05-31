package com.juliamartyn.goldenbook.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application-test.properties"})
@ActiveProfiles("test")
public class OrderControllerTest {
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
    public void findAllEndpointWithRoleAdmin() throws Exception {
        mvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void findAllEndpointWithRoleSeller() throws Exception {
        mvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void findAllEndpointWithRoleCustomer() throws Exception {
        mvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("customer")
    public void findAllForCurrentUserEndpointWithRoleCustomer() throws Exception {
        mvc.perform(get("/api/orders/current-user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void findAllForCurrentUserEndpointWithRoleSeller() throws Exception {
        mvc.perform(get("/api/orders/current-user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("customer")
    public void cartEndpointWithRoleCustomer() throws Exception {
        mvc.perform(get("/api/orders/cart")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void cartEndpointWithRoleSeller() throws Exception {
        mvc.perform(get("/api/orders/cart")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("seller")
    public void preOrdersEndpointWithRoleSeller() throws Exception {
        mvc.perform(get("/api/orders/pre-orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("customer")
    public void preOrdersEndpointWithRoleCustomer() throws Exception {
        mvc.perform(get("/api/orders/pre-orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @WithUserDetails("admin")
    public void updateStatusEndpointWithRoleAdmin() throws Exception {
        mvc.perform(patch("/api/orders/1/status")
                .content("{\"status\":\"ORDERED\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void updateStatusEndpointWithRoleSeller() throws Exception {
        mvc.perform(patch("/api/orders/1/status")
                .content("{\"status\":\"ORDERED\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void updateStatusEndpointWithRoleCustomer() throws Exception {
        mvc.perform(patch("/api/orders/2/status")
                .content("{\"status\":\"ORDERED\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("seller")
    public void addBookEndpointWithRoleSeller() throws Exception {
        mvc.perform(put("/api/orders/add-book/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("customer")
    public void addBookEndpointWithRoleCustomer() throws Exception {
        mvc.perform(put("/api/orders/add-book/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void deleteBookFromCartEndpointWithRoleSeller() throws Exception {
        mvc.perform(patch("/api/orders/1/cart/delete-book/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("customer")
    public void deleteBookFromCartEndpointWithRoleCustomer() throws Exception {
        mvc.perform(patch("/api/orders/1/cart/delete-book/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
