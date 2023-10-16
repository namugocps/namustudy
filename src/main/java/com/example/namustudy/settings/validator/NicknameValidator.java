package com.example.namustudy.settings.validator;

import com.example.namustudy.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NicknameValidator {

    private final AccountRepository accountRepository;
}
