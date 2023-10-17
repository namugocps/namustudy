package com.example.namustudy.settings.validator;

import com.example.namustudy.account.AccountRepository;
import com.example.namustudy.settings.form.NicknameForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NicknameValidator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz){
        return NicknameForm.class.isAssignableFrom(clazz);
    }
}
