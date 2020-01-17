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

/**
 * Контроллер - осуществляет маппинг методов и вызовов API. Реализует доступ к методам {@link PHMessageRepository}
 * Корневой путь устанавливается: /api
 * 
 * @author Andrei Chuchalov
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/api")
public class PHMessageController {
	
	private static Logger log = LoggerFactory.getLogger(PHMessageController.class);
	private static final String msm = "Message sent to ESB.";
	@Value("${pmh.redis.topic}") String topic;
	
	@Autowired
	private PHMessageProducer phMessageProducer;
	private PHMessageCommonRepository<PHMessage> phMessageRepository;

	/**
	 * Конструктор контроллера
	 * @param phMessageRepository объект - репозиторий сущностей
	 * @param phMessageProducer ссылка на класс отсылки сообщений на Redis-server
	 */
	public PHMessageController(PHMessageCommonRepository<PHMessage> phMessageRepository,
			PHMessageProducer phMessageProducer) {
		this.phMessageRepository = phMessageRepository;
		this.phMessageProducer = phMessageProducer;
	}

	/**
	 * Метод выдает список всех сущностей из репозитория
	 * @return результат выполнения операции {@link ResponseEntity#ok()}
	 */
	@GetMapping("/messages")
	public ResponseEntity<Iterable<PHMessage>> getToDos() {
		return ResponseEntity.ok(phMessageRepository.findAll());
	}

	/**
	 * Метод выдает выбранное по id сообщение из репозитория
	 * @param id идентификатор сообщения
	 * @return результат выполнения операции {@link ResponseEntity#ok()}
	 */
	@GetMapping("/messages/{id}")
	public ResponseEntity<PHMessage> getToDoById(@PathVariable String id) {
		return ResponseEntity.ok(phMessageRepository.findById(id));
	}
	
	/**
	 * Инициирование отправки сущностей в репозитории в очередь Redis-server для модификации
	 * @return результат выполнения операции {@link ResponseEntity#ok()}
	 */
	@GetMapping("/messages/send")
	public ResponseEntity<String> sendMessagesToQueue() {
		log.info("Trying to send messages...");
		log.info(phMessageRepository.toString());
		phMessageRepository.findAll().forEach(message -> phMessageProducer.sendTo(topic, message));
		return ResponseEntity.ok(msm);
	}

	/**
	 * Метод инициирует изменение сущности на предмет установки флага ошибки
	 * @param id идентификатор сообщения
	 * @return результат выполнения операции {@link ResponseEntity#ok()}
	 */
	@PatchMapping("/messages/error/{id}")
	public ResponseEntity<PHMessage> setError(@PathVariable String id) {
		PHMessage result = phMessageRepository.findById(id);
		result.setError(true);
		phMessageRepository.save(result);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(result.getId()).toUri();
		return ResponseEntity.ok().header("Location", location.toString()).build();
	}

	/**
	 * Метод инициирует изменение сущности на предмет установки флага валидности
	 * @param id идентификатор сообщения
	 * @return результат выполнения операции {@link ResponseEntity#ok()}
	 */
	@PatchMapping("/messages/invalid/{id}")
	public ResponseEntity<PHMessage> setInvalid(@PathVariable String id) {
		PHMessage result = phMessageRepository.findById(id);
		result.setValid(false);
		phMessageRepository.save(result);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(result.getId()).toUri();
		return ResponseEntity.ok().header("Location", location.toString()).build();
	}

	/**
	 * Метод создает сущность
	 * @param phMessage созданная сущность {@link PHMessage}
	 * @param errors перечень ошибок, возникших при обработке запроса
	 * @return результат выполнения операции {@link ResponseEntity#created(URI)}
	 */
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
	
	/**
	 * Метод удаляет сущность по идентификатору
	 * @param id идентификатор сущности
	 * @return результат выполнения операции {@link ResponseEntity#noContent()}
	 */
	@DeleteMapping("/messages/{id}")
	public ResponseEntity<PHMessage> deletePhMessage(@PathVariable String id) {
		phMessageRepository.delete(PHMessageBuilder.create().withId(id).build());
		return ResponseEntity.noContent().build();
	}

	/**
	 * Метод удаляет сущность
	 * @param phMessage сущность
	 * @return результат выполнения операции {@link ResponseEntity#noContent()}
	 */
	@DeleteMapping("/messages")
	public ResponseEntity<PHMessage> deletePhMessage(@RequestBody PHMessage phMessage) {
		phMessageRepository.delete(phMessage);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Метод обработчик ошибок выполнения {@link HttpStatus#BAD_REQUEST}
	 * 
	 * @param exception ссылка на исключение
	 * @return обработчик ошибки
	 */
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public PHMessageValidationError handleException(Exception exception) {
		return new PHMessageValidationError(exception.getMessage());
	}

}
