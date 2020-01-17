package ru.chuchalov.messaging;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс - основная сущность проекта: сообщение, над которым совершаются реализованные Use case
 * Содержит основные поля: {@link PHMessage#id}, {@link PHMessage#message}, {@link PHMessage#valid}, {@link PHMessage#error}
 * 
 * @author Andrei Chuchalov
 * @version 1.0
 *
 */
@Data
public class PHMessage {
	
	/**
	 * Поле содержит уникальный строковый идентификатор сообщения
	 */
	@NotNull
	private String id;
	/**
	 * Поле содержит тело сообщения
	 */
	private String message;
	/**
	 * Метка корректности сообщения
	 */
	private boolean valid;
	/**
	 * Метка характера сообщения: ошибка
	 */
	private boolean error;

	/**
	 * Конструктор без параметров: создает сообщение с автоматически генерируемым
	 * полем {@link PHMessage#id}, пустым полем {@link PHMessage#message}, 
	 * {@link PHMessage#error} = false, {@link PHMessage#valid} = true
	 */
	public PHMessage() {
		this.id = UUID.randomUUID().toString();
		this.message = "";
		this.error = false;
		this.valid = true;
	}
	
	/**
	 * Конструктор создает сообщение, устанавливает начальное сообщение {@link PHMessage#message}
	 * 
	 * @param message Инициирующее сообщение
	 */
	public PHMessage(String message) {
		this();
		this.message = message;
	}
	
	/**
	 * Возвращает значение поля {@link PHMessage#message}
	 * @return строковое значение
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Возвращает значеение поля {@link PHMessage#id}
	 * @return строковое значение
	 */
	public String getId() {
		return id;
	}

	/**
	 * Возвращает значение поля {@link PHMessage#valid}
	 * @return логическое значение
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Возвращает значение поля {@link PHMessage#error}
	 * @return логическое значение
	 */
	public boolean isError() {
		return error;
	}
	
	/**
	 * Устанавливает значение поля {@link PHMessage#message}
	 * @param id строковое значение
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Устанавливает значение поля {@link PHMessage#id}
	 * @param message строковое значение
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Устанавливает значение поля {@link PHMessage#valid}
	 * @param valid логическое выражение
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * Устанавливает значение поля {@link PHMessage#error}
	 * @param error логическое выражение
	 */
	public void setError(boolean error) {
		this.error = error;
	}
	
	/**
	 * Метод проверяет идентичность объектов на основании попарного сравнения полей класса
	 * @param obj объект для сравнения
	 */
	public boolean equals(Object obj) {
		if(!(obj instanceof PHMessage)) return false;
		PHMessage msg = (PHMessage)obj;
		return (msg.getId().equals(this.id))&&(msg.getMessage().equals(this.message))
				&&(msg.isError()==this.error)&&(msg.isValid()==this.valid);
	}

}
