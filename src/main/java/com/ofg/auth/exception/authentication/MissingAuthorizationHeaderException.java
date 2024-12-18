package com.ofg.auth.exception.authentication;

import com.ofg.auth.core.util.message.Messages;
import org.springframework.context.i18n.LocaleContextHolder;

public class MissingAuthorizationHeaderException extends RuntimeException {
    public MissingAuthorizationHeaderException() {
        super(Messages.getMessageForLocale("app.msg.auth.header.missing", LocaleContextHolder.getLocale()));
    }
}