package com.example.namustudy;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {
    @Override
    public SecurityContext createSecurityContext(WithAccount annotation) {
        return null;
    }
}
