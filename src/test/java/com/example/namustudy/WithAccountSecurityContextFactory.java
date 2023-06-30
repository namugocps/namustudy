package com.example.namustudy;

import com.example.namustudy.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    private final AccountService accountService;
    @Override
    public SecurityContext createSecurityContext(WithAccount annotation) {
        return null;
    }
}
