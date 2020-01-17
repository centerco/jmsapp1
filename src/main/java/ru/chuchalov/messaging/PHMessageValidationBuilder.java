package ru.chuchalov.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class PHMessageValidationBuilder {
	private static Logger log = LoggerFactory.getLogger(PHMessageValidationBuilder.class);
    public static PHMessageValidationError fromBindingErrors(Errors errors) {
        PHMessageValidationError error = new PHMessageValidationError("Validation failed. " + 
        		errors.getErrorCount() + " error(s)");
        for (ObjectError objectError : errors.getAllErrors()) {
            error.addValidationError(objectError.getDefaultMessage());
            log.info(objectError.getDefaultMessage());
        }
        return error;
    }
}
