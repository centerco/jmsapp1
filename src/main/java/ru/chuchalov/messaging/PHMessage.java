package ru.chuchalov.messaging;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PHMessage {
	
	@NotNull
	private String id;
	private String message;
	private boolean valid;
	private boolean error;

	public PHMessage() {
		this.id = UUID.randomUUID().toString();
		this.message = "";
		this.error = false;
		this.valid = true;
	}
	
	public PHMessage(String message) {
		this();
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public String getId() {
		return id;
	}

	public boolean isValid() {
		return valid;
	}

	public boolean isError() {
		return error;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public void setError(boolean error) {
		this.error = error;
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof PHMessage)) return false;
		PHMessage msg = (PHMessage)obj;
		return (msg.getId().equals(this.id))&&(msg.getMessage().equals(this.message))
				&&(msg.isError()==this.error)&&(msg.isValid()==this.valid);
	}

}
