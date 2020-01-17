package ru.chuchalov.messaging;

import java.net.URI;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class PHMessageController {
	
	private static Logger log = LoggerFactory.getLogger(PHMessageController.class);
	private static final String msm = "Message sent to ESB.";
	@Value("${pmh.redis.topic}") String topic;
	
	@Autowired
	private PHMessageProducer phMessageProducer;
	private PHMessageCommonRepository<PHMessage> phMessageRepository;

	public PHMessageController(PHMessageCommonRepository<PHMessage> phMessageRepository,
			PHMessageProducer phMessageProducer) {
		this.phMessageRepository = phMessageRepository;
		this.phMessageProducer = phMessageProducer;
	}

	@GetMapping("/messages")
	public ResponseEntity<Iterable<PHMessage>> getToDos() {
		return ResponseEntity.ok(phMessageRepository.findAll());
	}

	@GetMapping("/messages/{id}")
	public ResponseEntity<PHMessage> getToDoById(@PathVariable String id) {
		return ResponseEntity.ok(phMessageRepository.findById(id));
	}
	
	@GetMapping("/messages/send")
	public ResponseEntity<String> sendMessagesToQueue() {
		log.info("Trying to send messages...");
		log.info(phMessageRepository.toString());
		phMessageRepository.findAll().forEach(message -> phMessageProducer.sendTo(topic, message));
		return ResponseEntity.ok(msm);
	}

	@PatchMapping("/messages/error/{id}")
	public ResponseEntity<PHMessage> setError(@PathVariable String id) {
		PHMessage result = phMessageRepository.findById(id);
		result.setError(true);
		phMessageRepository.save(result);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(result.getId()).toUri();
		return ResponseEntity.ok().header("Location", location.toString()).build();
	}

	@PatchMapping("/messages/invalid/{id}")
	public ResponseEntity<PHMessage> setInvalid(@PathVariable String id) {
		PHMessage result = phMessageRepository.findById(id);
		result.setValid(false);
		phMessageRepository.save(result);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(result.getId()).toUri();
		return ResponseEntity.ok().header("Location", location.toString()).build();
	}

	@RequestMapping(value = "/messages", method = { RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<?> createPHMessage(@Valid @RequestBody PHMessage phMessage, Errors errors) {
		log.debug("Just entered createPHMessage");
		if (errors.hasErrors()) {
			log.debug("Errors found!"+errors.toString());
			return ResponseEntity.badRequest().body(PHMessageValidationBuilder.fromBindingErrors(errors));
		}
		log.debug("phMessage>> "+phMessage);
		PHMessage result = phMessageRepository.save(phMessage);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/messages/{id}")
	public ResponseEntity<PHMessage> deletePhMessage(@PathVariable String id) {
		phMessageRepository.delete(PHMessageBuilder.create().withId(id).build());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/messages")
	public ResponseEntity<PHMessage> deletePhMessage(@RequestBody PHMessage phMessage) {
		phMessageRepository.delete(phMessage);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public PHMessageValidationError handleException(Exception exception) {
		return new PHMessageValidationError(exception.getMessage());
	}

}
