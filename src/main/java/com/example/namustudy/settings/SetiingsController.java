package com.example.namustudy.settings;

import com.example.namustudy.account.CurrentUser;
import com.example.namustudy.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SetiingsController {

    @GetMapping("/settings/profile")
    public void profileUpdateForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new Profile(account));
    }
}
