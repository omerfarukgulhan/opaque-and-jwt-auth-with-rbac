package com.ofg.auth.exception.authentication;

import com.ofg.auth.core.util.message.Messages;
import org.springframework.context.i18n.LocaleContextHolder;

public class UserInactiveException extends RuntimeException {
    public UserInactiveException() {
        super(Messages.getMessageForLocale("app.msg.user.inactive", LocaleContextHolder.getLocale()));
    }
}