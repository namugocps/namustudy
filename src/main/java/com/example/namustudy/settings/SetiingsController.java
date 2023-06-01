package com.example.namustudy.settings;

import com.example.namustudy.account.CurrentUser;
import com.example.namustudy.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class SetiingsController {

    private static final String SETTINGS_PROFILE_VIEW_NAME = "settings/profile";

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new Profile(account));
        return SETTINGS_PROFILE_VIEW_NAME;
    }

    @PostMapping("/settings/profile")
    public String updateProfile(@CurrentUser Account account, @Valid Profile profile, Errors errors, Model model){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTINGS_PROFILE_VIEW_NAME;
        }
    }
}
