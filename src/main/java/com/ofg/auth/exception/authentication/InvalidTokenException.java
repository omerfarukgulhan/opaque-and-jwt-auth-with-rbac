package com.ofg.auth.exception.authentication;

import com.ofg.auth.core.util.message.Messages;
import org.springframework.context.i18n.LocaleContextHolder;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String tokenType) {
        super(Messages.getMessageForLocale(
                switch (tokenType) {
                    case "reset" -> "app.msg.password.reset.invalid.token";
                    case "activation" -> "app.msg.activate.user.invalid.token";
                    default -> "app.msg.invalid.token";
                },
                LocaleContextHolder.getLocale()
        ));
    }
}