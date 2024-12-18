package com.ofg.auth.exception.authentication;

import com.ofg.auth.core.util.message.Messages;
import org.springframework.context.i18n.LocaleContextHolder;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super(Messages.getMessageForLocale("app.msg.auth.token.expired", LocaleContextHolder.getLocale()));
    }
}
