package com.example.namustudy.main;

import com.example.namustudy.account.AccountService;
import com.example.namustudy.account.SignUpForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;
    
    @Test
    void login_with_email() throws Exception{
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("seokwon");
        signUpForm.setEmail("seokwon@email.com");
        signUpForm.setPassword("12345678");
        accountService.processNewAccount(signUpForm);

        mockMvc.perform(post("login")
                .param("username","seokwon@email.com")
                .param("password","12345678")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        
    }
}