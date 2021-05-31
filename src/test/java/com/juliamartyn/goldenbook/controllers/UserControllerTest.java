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
public class UserControllerTest {
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
        mvc.perform(post("/api/users")
                .content("{\"username\":\"username\",\"password\":\"password\"," +
                        "\"email\":\"email\", \"phone\": \"000\", \"role\": \"ROLE_SELLER\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails("seller")
    public void createWithRoleSeller() throws Exception {
        mvc.perform(post("/api/users")
                .content("{\"username\":\"username\",\"password\":\"password\"," +
                        "\"email\":\"email\", \"phone\": \"000\", \"role\": \"ROLE_SELLER\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("customer")
    public void createWithRoleCustomer() throws Exception {
        mvc.perform(post("/api/users")
                .content("{\"username\":\"username\",\"password\":\"password\"," +
                        "\"email\":\"email\", \"phone\": \"000\", \"role\": \"ROLE_SELLER\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("admin")
    public void findAllWithRoleAdmin() throws Exception {
        mvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void findAllWithRoleSeller() throws Exception {
        mvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void findAllWithRoleCustomer() throws Exception {
        mvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("admin")
    public void findByIdWithRoleAdmin() throws Exception {
        mvc.perform(get("/api/users/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void findByIdWithRoleCustomer() throws Exception {
        mvc.perform(get("/api/users/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void findByIdWithRoleSeller() throws Exception {
        mvc.perform(get("/api/users/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


   @Test
   @WithUserDetails("admin")
   public void pageableUsersWithRoleAdmin() throws Exception {
       mvc.perform(get("/api/users/page/1")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
   }

    @Test
    @WithUserDetails("seller")
    public void pageableUsersWithRoleSeller() throws Exception {
        mvc.perform(get("/api/users/page/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer")
    public void pageableUsersWithRoleCustomer() throws Exception {
        mvc.perform(get("/api/users/page/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("admin")
    public void updateDisabledWithRoleAdmin() throws Exception {
        mvc.perform(patch("/api/users/4/disabled")
                .content("{\"disabled\":\"true\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void updateDisabledWithRoleSeller() throws Exception {
        mvc.perform(patch("/api/users/4/disabled")
                .content("{\"disabled\":\"true\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("customer")
    public void updateDisabledWithRoleCustomer() throws Exception {
        mvc.perform(patch("/api/users/4/disabled")
                .content("{\"disabled\":\"true\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("admin")
    public void updateUsernameWithRoleAdmin() throws Exception {
        mvc.perform(patch("/api/users/4/username")
                .content("{\"username\":\"usernameU\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("seller")
    public void updateUsernameWithRoleSeller() throws Exception {
        mvc.perform(patch("/api/users/4/username")
                .content("{\"username\":\"usernameU\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("customer")
    public void updateUsernameWithRoleCustomer() throws Exception {
        mvc.perform(patch("/api/users/4/username")
                .content("{\"username\":\"usernameU\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("admin")
    public void updateDeliveryAddressWithRoleAdmin() throws Exception {
        mvc.perform(patch("/api/users/3/delivery-address")
                .content("{\"deliveryAddress\":\"delivery address\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("seller")
    public void updateDeliveryAddressWithRoleSeller() throws Exception {
        mvc.perform(patch("/api/users/3/delivery-address")
                .content("{\"deliveryAddress\":\"delivery address\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("customer")
    public void updateDeliveryAddressWithRoleCustomer() throws Exception {
        mvc.perform(patch("/api/users/3/delivery-address")
                .content("{\"deliveryAddress\":\"delivery address\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
