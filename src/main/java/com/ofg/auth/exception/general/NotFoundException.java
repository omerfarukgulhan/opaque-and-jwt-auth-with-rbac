package com.ofg.auth.exception.general;

import com.ofg.auth.core.util.message.Messages;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super(Messages.getMessageForLocale("app.msg.data.id.not.found", LocaleContextHolder.getLocale(), id));
    }

    public NotFoundException(int value) {
        super(Messages.getMessageForLocale("app.msg.data.id.not.found", LocaleContextHolder.getLocale(), value));
    }

    public NotFoundException(String data) {
        super(Messages.getMessageForLocale("app.msg.data.not.found", LocaleContextHolder.getLocale(), data));
    }
}