package com.example.namustudy.settings;

import com.example.namustudy.account.AccountRepository;
import com.example.namustudy.account.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SetiingsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @DisplayName("프로필 수정하기 - 입력값 정상")
    @Test
    void updateProfile() throws Exception{
        mockMvc.perform(post(SetiingsController.SETTINGS_PROFILE_URL)
                .param("bio", "짧은 소개를 수정하는 경우."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SetiingsController.SETTINGS_PROFILE_URL))
                .andExpect(flash().attributeExists("message"));
    }
}