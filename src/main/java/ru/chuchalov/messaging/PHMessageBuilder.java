package ru.chuchalov.messaging;

/**
 * Строитель для пошагового создания {@link PHMessage}
 * 
 * @author Andrei Chuchalov
 * @version 1.0
 */
public class PHMessageBuilder {
	
	/**
	 * Статический инстанс класса
	 */
	private static PHMessageBuilder instance = new PHMessageBuilder();
	
	/**
	 * Строковый идентификатор. См. {@link PHMessage}
	 */
	private String id = null;
	
	/**
	 * Строковый идентификатор. См. {@link PHMessage}
	 */
	private String message = null;

	private PHMessageBuilder(){}

	/**
	 * Возвращает статический инстанс класса
	 * @return экземпляр класса
	 */
	public static PHMessageBuilder create() {
		return instance;
	}

	/**
	 * Добавляет параметр id
	 * @param id идентификатор сообщения
	 * @return экземпляр класса
	 */
	public PHMessageBuilder withId(String id) {
		this.id = id;
		return instance;
	}
	
	/**
	 * Добавляет параметр message
	 * @param message тело сообщения
	 * @return экземпляр класса
	 */
	public PHMessageBuilder withMessage(String message) {
		this.message = message;
		return instance;
	}

	/**
	 * Возвращает {@link PHMessage} с набором заданных строителем параметров
	 * @return {@link PHMessage}
	 */
	public PHMessage build() {
		PHMessage result = new PHMessage();
		
		if(id != null) result.setId(id);
		if(message != null) result.setMessage(message);
		
		return result;
	}
}
