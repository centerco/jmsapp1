package ru.chuchalov.messaging;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Обработка ошибок преобразования запросов
 * @author Andrei Chuchalov
 * @version 1.0
 *
 */
public class PHMessageValidationError {
	
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors = new ArrayList<>();
    private final String errorMessage;
    
    /**
     * Конструктор сохраняет сообщение об ошибке
     * @param errorMessage
     */
    public PHMessageValidationError(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    /**
     * Формируется блок исключений
     * @param error сообщение об исключении
     */
    public void addValidationError(String error) {
        errors.add(error);
    }
    
    /**
     * Возвращает список исключений
     * @return список строковых сообщений
     */
    public List<String> getErrors() {
        return errors;
    }
    
    /**
     * Возвращает сообщение об ошибке
     * @return строковое значение
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
