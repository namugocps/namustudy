package com.example.namustudy.settings.validator;

import com.example.namustudy.settings.PasswordForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PasswordFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PasswordForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordForm passwordForm = (PasswordForm)target;
        if(!passwordForm.getNewPassword().equals(passwordForm.getNewPasswordConfrim())){
            errors.rejectValue("newPassword","wrong.value", "입력한 새 패스워드가 일치하지 않습니다.");
        }

    }
}
