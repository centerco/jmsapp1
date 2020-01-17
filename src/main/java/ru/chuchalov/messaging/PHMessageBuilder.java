package ru.chuchalov.messaging;

public class PHMessageBuilder {
	private static PHMessageBuilder instance = new PHMessageBuilder();
	private String id = null;
	private String message = null;

	private PHMessageBuilder(){}

	public static PHMessageBuilder create() {
		return instance;
	}

	public PHMessageBuilder withId(String id) {
		this.id = id;
		return instance;
	}
	
	public PHMessageBuilder withMessage(String message) {
		this.message = message;
		return instance;
	}

	public PHMessage build() {
		PHMessage result = new PHMessage();
		
		if(id != null) result.setId(id);
		if(message != null) result.setMessage(message);
		
		return result;
	}
}
