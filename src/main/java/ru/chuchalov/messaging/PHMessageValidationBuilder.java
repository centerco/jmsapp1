package ru.chuchalov.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

/**
 * Класс используется для обработки исключений при передаче параметров функций преобразования сообщений
 * @author Andrei Chuchalov
 * @version 1.0
 *
 */
public class PHMessageValidationBuilder {
	
	private static Logger log = LoggerFactory.getLogger(PHMessageValidationBuilder.class);
	
	/**
	 * Метод обработки сообщения об ошибках преобразования сообщений
	 * @param errors перечень исключений
	 * @return {@link PHMessageValidationError}
	 */
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
