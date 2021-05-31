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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application-test.properties"})
@ActiveProfiles("test")
public class ReadAndReturnControllerTest {
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
    @WithUserDetails("seller")
    public void createWithRoleSeller() throws Exception {
        mvc.perform(post("/api/read-return")
                .content("{\"bookId\":\"3\",\"pricePerDay\":\"3\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails("customer")
    public void createWithRoleCustomer() throws Exception {
        mvc.perform(post("/api/read-return")
                .content("{\"bookId\":\"3\",\"pricePerDay\":\"3\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    public void createWithRoleAdmin() throws Exception {
        mvc.perform(post("/api/read-return")
                .content("{\"bookId\":\"3\",\"pricePerDay\":\"3\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("seller")
    public void updatePricePerDayWithRoleSeller() throws Exception {
        mvc.perform(patch("/api/read-return/1/price-per-day")
                .content("{\"pricePerDay\":\"1\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("admin")
    public void updatePricePerDayWithRoleAdmin() throws Exception {
        mvc.perform(patch("/api/read-return/1/price-per-day")
                .content("{\"pricePerDay\":\"1\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("customer")
    public void updatePricePerDayWithRoleCustomer() throws Exception {
        mvc.perform(patch("/api/read-return/1/price-per-day")
                .content("{\"pricePerDay\":\"1\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("seller")
    public void findAllWithRoleSeller() throws Exception {
        mvc.perform(get("/api/read-return")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void findAllWithRoleCustomer() throws Exception {
        mvc.perform(get("/api/read-return")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void findAllRentedBookWithRoleSeller() throws Exception {
        mvc.perform(get("/api/read-return/rented")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void findAllRentedBookWithRoleCustomer() throws Exception {
        mvc.perform(get("/api/read-return/rented")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("customer")
    public void rentBookWithRoleCustomer() throws Exception {
        mvc.perform(post("/api/read-return/rent")
                .content("{\"tariffId\":\"1\",\"rentDaysNumber\":\"2\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void rentBookWithRoleSeller() throws Exception {
        mvc.perform(post("/api/read-return/rent")
                .content("{\"tariffId\":\"3\",\"rentDaysNumber\":\"3\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    public void rentBookWithRoleAdmin() throws Exception {
        mvc.perform(post("/api/read-return/rent")
                .content("{\"tariffId\":\"3\",\"rentDaysNumber\":\"3\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("customer")
    public void updateEmailRemindingWithRoleCustomer() throws Exception {
        mvc.perform(patch("/api/read-return/1/email-reminding")
                .content("{\"emailReminding\":\"true\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("admin")
    public void updateEmailRemindingWithRoleAdmin() throws Exception {
        mvc.perform(patch("/api/read-return/1/email-reminding")
                .content("{\"emailReminding\":\"true\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("seller")
    public void updateEmailRemindingWithRoleSeller() throws Exception {
        mvc.perform(patch("/api/read-return/1/email-reminding")
                .content("{\"emailReminding\":\"true\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
