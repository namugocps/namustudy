package com.example.namustudy.settings;

import com.example.namustudy.account.AccountService;
import com.example.namustudy.account.CurrentAccount;
import com.example.namustudy.account.CurrentUser;
import com.example.namustudy.domain.Account;
import com.example.namustudy.domain.Tag;
import com.example.namustudy.domain.Zone;
import com.example.namustudy.settings.form.NicknameForm;
import com.example.namustudy.settings.form.TagForm;
import com.example.namustudy.settings.form.ZoneForm;
import com.example.namustudy.settings.validator.NicknameValidator;
import com.example.namustudy.settings.validator.PasswordFormValidator;
import com.example.namustudy.tag.TagRepository;
import com.example.namustudy.zone.ZoneRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.namustudy.settings.SetiingsController.ROOT;
import static com.example.namustudy.settings.SetiingsController.SETTINGS;

@Controller
@RequestMapping(ROOT + SETTINGS)
@RequiredArgsConstructor
public class SetiingsController {

    static final String ROOT = "/";
    static final String SETTINGS = "settings";
    static final String PROFILE = "/profile";
    static final String PASSWORD = "/password";
    static final String NOTIFICATIONS = "/notifications";
    static final String TAGS = "/tags";
    static final String ZONES = "/zones";
    static final String ACCOUNT = "/account";
    static final String SETTINGS_PROFILE_URL = "/settings/profile";
    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final NicknameValidator nicknameValidator;
    private final TagRepository tagRepository;
    private final ZoneRepository zoneRepository;
    private final ObjectMapper objectMapper;

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @InitBinder("nicknameForm")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(nicknameValidator);
    }

    @GetMapping(PROFILE)
    public String updateProfileForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Profile.class));
        return SETTINGS + PROFILE;
    }

    @PostMapping(PROFILE)
    public String updateProfile(@CurrentUser Account account, @Valid Profile profile, Errors errors, Model model,
                                RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTINGS + PROFILE;
        }

        accountService.updateProfile(account, profile);
        attributes.addFlashAttribute("message","프로필을 수정했습니다.");
        return "redirect:" +SETTINGS + PROFILE;
    }

    @GetMapping(SETTINGS + PASSWORD)
    public String updatePasswordForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());
        return SETTINGS + PASSWORD;
    }

    @PostMapping(SETTINGS + PASSWORD)
    public String updatePassword(@CurrentUser Account account, @Valid PasswordForm passwordForm
            , Errors errors, Model model, RedirectAttributes attributes){

        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTINGS + PASSWORD;
        }

        accountService.updatePassword(account, passwordForm.getNewPassword());
        attributes.addFlashAttribute("message", "패스워드를 변경했습니다.");
        return "redirect:" + SETTINGS + PASSWORD;
    }

    @GetMapping(SETTINGS+NOTIFICATIONS)
    public String updateNotificationsForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Notifications.class));
        return SETTINGS+NOTIFICATIONS;
    }

    @PostMapping(SETTINGS + NOTIFICATIONS)
    public String updateNotifications(@CurrentUser Account account, @Valid Notifications notifications, Errors errors, Model model,
                                      RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTINGS+NOTIFICATIONS;
        }

        accountService.updateNotifications(account, notifications);
        attributes.addFlashAttribute("message", "알림 설정을 변경했습니다.");
        return "redirect:" + SETTINGS+NOTIFICATIONS;
    }

    @GetMapping(SETTINGS + TAGS)
    public String updateTags(@CurrentUser Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        Set<Tag> tags =accountService.getTags(account);
        model.addAttribute("tags",tags.stream().map(Tag::getTitle).collect(Collectors.toList()));

        List<String> allTags = tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        return SETTINGS+TAGS;
    }

    @PostMapping(SETTINGS + TAGS +"/add")
    @ResponseBody
    public ResponseEntity addTag(@CurrentUser Account account, @RequestBody TagForm tagForm){
        String title = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(title);
        if (tag == null){
            tag  = tagRepository.save(Tag.builder().title(title).build());
        }

        accountService.addTag(account, tag);
        return ResponseEntity.ok().build();
    }



    @PostMapping(TAGS + "/remove")
    @ResponseBody
    public ResponseEntity removeTag(@CurrentAccount Account account, @RequestBody TagForm tagForm){
        String title = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(title);
        if (tag == null){
            return ResponseEntity.badRequest().build();
        }
        accountService.removeTag(account, tag);
        return ResponseEntity.ok().build();
    }


    @GetMapping(ZONES)
    public String updateZonesForm(@CurrentAccount Account account, Model model) throws JsonProcessingException{
        model.addAttribute(account);

        Set<Zone> zones = accountService.getZones(account);
        model.addAttribute("zones", zones.stream().map(Zone::toString).collect(Collectors.toList()));

        List<String> allZones = zoneRepository.findAll().stream().map(Zone::toString).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allZones));

        return SETTINGS + ZONES;
    }

    @PostMapping(ZONES + "/add")
    @ResponseBody
    public ResponseEntity addZone(@CurrentAccount Account account, @RequestBody ZoneForm zoneForm){
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        if (zone ==null){
            return ResponseEntity.badRequest().build();
        }

        accountService.addZone(account, zone);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ZONES + "/remove")
    @ResponseBody
    public ResponseEntity removeZone(@CurrentAccount Account account, @RequestBody ZoneForm zoneForm){
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        if (zone ==null){
            return ResponseEntity.badRequest().build();
        }

        accountService.removeZone(account, zone);
        return ResponseEntity.ok().build();
    }

    @GetMapping(SETTINGS + "/account")
    public String updateAccountForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, NicknameForm.class));
        return SETTINGS+ACCOUNT;
    }

    @PostMapping(SETTINGS+ACCOUNT)
    public String updateAccount(@CurrentUser Account account, @Valid NicknameForm nicknameForm, Errors errors,
                                    Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTINGS + ACCOUNT;
        }

        accountService.updateNickname(account, nicknameForm.getNickname());
        attributes.addFlashAttribute("message","닉네임을 수정했습니다.");
        return "redirect:" + SETTINGS + ACCOUNT;
    }
}
