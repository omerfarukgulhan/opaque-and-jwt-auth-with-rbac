package com.ofg.auth.exception.file;

import com.ofg.auth.core.util.message.Messages;
import org.springframework.context.i18n.LocaleContextHolder;

public class FileServiceException extends RuntimeException {
    public FileServiceException(String errorType) {
        super(Messages.getMessageForLocale(
                switch (errorType) {
                    case "save" -> "app.msg.file.save.error";
                    case "save64" -> "app.msg.file.save64.error";
                    case "image null" -> "app.msg.file.image.null.error";
                    case "delete" -> "app.msg.file.delete.error";
                    default -> "app.msg.file.service.error";
                },
                LocaleContextHolder.getLocale()
        ));
    }
}