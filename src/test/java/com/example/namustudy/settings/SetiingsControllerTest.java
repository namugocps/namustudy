package com.example.namustudy.settings;

import com.example.namustudy.WithAccount;
import com.example.namustudy.account.AccountRepository;
import com.example.namustudy.account.AccountService;
import com.example.namustudy.domain.Account;
import com.example.namustudy.domain.Tag;
import com.example.namustudy.domain.Zone;
import com.example.namustudy.settings.form.TagForm;
import com.example.namustudy.settings.form.ZoneForm;
import com.example.namustudy.tag.TagRepository;
import com.example.namustudy.zone.ZoneRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static com.example.namustudy.settings.SetiingsController.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class SetiingsControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AccountService accountService;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ZoneRepository zoneRepository;

    private Zone testZone = Zone.builder().city("test").localNameOfCity("테스트시").province("테스트주").build();

    @AfterEach
    void afterEach(){
        accountRepository.deleteAll();
        zoneRepository.delteAll();
    }

    @BeforeEach
    void beforeEach(){
        zoneRepository.save(testZone);
    }

    @WithAccount("seokwon")
    @DisplayName("계정에 태그 추가")
    @Test
    void removeZone() throws Exception{
        Account seokwon = accountRepository.findByNickname("seokwon");
        Zone zone = zoneRepository.findByCityAndProvince(testZone.getCity(), testZone.getProvinc());
        accountRepository.addTag(seokwon, zone);

        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post(ROOT+SETTINGS + ZONES + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(tagForm))
                        .with(csrf()))
                .andExpect(status().isOk());
        assertFalse(seokwon.getTags().contains(zone));
    }


    @WithAccount("seokwon")
    @DisplayName("계정의 지역 정보 수정 폼")
    @Test
    void updateZonesForm() throws Exception{
        mockMvc.perform(get(ROOT + SETTINGS + ZONES))
                .andExpect(view().name(SETTINGS + ZONES))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("zones"));
    }

    @WithAccount("seokwon")
    @DisplayName("계정의 지역 정보 추가")
    @Test
    void addZone() throws Exception{
        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        mockMvc.perform(post(ROOT+SETTINGS + ZONES + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(zoneForm)));
    }



    @WithAccount("seokwon")
    @DisplayName("계정에 태그 수정 폼")
    @Test
    void updateTagsForm() throws Exception{
        mockMvc.perform(get(ROOT+ SETTINGS +TAGS))
                .andExpect(view().name(ROOT+ SETTINGS +TAGS))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("tags"));
    }


    @WithAccount("seokwon")
    @DisplayName("계정에 태그 추가")
    @Test
    void addTag() throws Exception{
        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post(ROOT+ SETTINGS +TAGS + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(tagForm))
                .with(csrf()))
                .andExpect(status().isOk());

        Tag newTag = tagRepository.findByTitle("newTag");
        assertNotNull(newTag);
        Account seokwon = accountRepository.findByNickname("seokwon");
        assertTrue(seokwon.getTags().contains(newTag));
    }

    @WithAccount("seokwon")
    @DisplayName("계정에 태그 삭제")
    @Test
    void removeTag() throws Exception{
        Account seokwon = accountRepository.findByNickname("seokwon");
        Tag newTag = tagRepository.save(Tag.builder().title("newTag").build());
        accountRepository.addTag(seokwon, newTag);

        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post(ROOT+ SETTINGS +TAGS  + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(tagForm))
                        .with(csrf()))
                .andExpect(status().isOk());
        assertFalse(seokwon.getTags().contains(newTag));
    }

    
    @WithAccount("seokwon")
    @DisplayName("프로필 수정 폼")
    @Test
    void updateProfileForm() throws Exception{
        mockMvc.perform(get(ROOT+ SETTINGS +PROFILE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    @WithAccount("seokwon")
    @DisplayName("프로필 수정하기 - 입력값 정상")
    @Test
    void updateProfile() throws Exception{
        String bio = "짧은 소개 수정";
        mockMvc.perform(post(ROOT+ SETTINGS +PROFILE)
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SETTINGS + PROFILE))
                .andExpect(flash().attributeExists("message"));

        Account kee = accountRepository.findByNickname("kee");
        assertEquals(bio, kee.getBio());
    }


    @WithAccount("seokwon")
    @DisplayName("프로필 수정하기 - 입력값 에러")
    @Test
    void updateProfile_error() throws Exception{
        String bio = "길게 소개를 수정하는 경우. 길게 소개를 수정하는 경우. 길게 소개를 수정하는 경우. 길게 소개를 수정하는 경우. 길게 소개를 수정하는 경우.";
        mockMvc.perform(post(ROOT+ SETTINGS +PROFILE)
                        .param("bio", bio)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SetiingsController.SETTINGS_PROFILE_URL))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account kee = accountRepository.findByNickname("seokwon");
        assertNull(kee.getBio());
    }


    @WithAccount("seokwon")
    @DisplayName("패스워드 수정 폼")
    @Test
    void updatePassword_form() throws Exception{
        mockMvc.perform(get(ROOT+ SETTINGS +PASSWORD))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }

    @WithAccount("seokwon")
    @DisplayName("패스워드 수정 - 입력값 정상")
    @Test
    void updatePassword_success() throws Exception{
        mockMvc.perform(get(ROOT+ SETTINGS +PASSWORD)
                .param("newPassword","12345678")
                .param("newPasswordConfirm","12345678")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SetiingsController.SETTINGS_PASSWORD_URL))
                .andExpect(flash().attributeExists("message"));

        Account seokwon = accountRepository.findByNickname("seokwon");
        assertTrue(passwordEncoder.matches("12345678",seokwon.getPassword()));
    }

    @WithAccount("seokwon")
    @DisplayName("패스워드 수정 - 입력값 에러 - 패스워드 불일치")
    @Test
    void updatePassword_fail() throws Exception{
        mockMvc.perform(get(ROOT+ SETTINGS +PASSWORD)
                .param("newPassword","12345678")
                .param("newPasswordConfirm","12345678")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SETTINGS + PASSWORD))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(model().attributeExists("account"));
    }
}