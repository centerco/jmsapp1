package ru.chuchalov.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Класс определяет обработку сообщений, полученных из очереди Redis-server
 * 
 * @author Andrei Chuchalov
 * @version 1.0
 *
 */
@Component
public class PHMessageConsumer {
	
    private static final Logger log = LoggerFactory.getLogger(PHMessageConsumer.class);
    private PHMessageRepository phMessageRepository;
    
    /**
     * Конструктор сохраняет ссылку на репозиторий {@link PHMessageRepository}
     * @param phMessageRepository репозиторий сущностей {@link PHMessageRepository}
     */
    public PHMessageConsumer(PHMessageRepository phMessageRepository){
        this.phMessageRepository = phMessageRepository;
    }
    
    /**
     * Метод - обработчик сущностей. Осуществляет модификацию сообщений
     * @param phMessage сущность в формате {@link PHMessage}
     */
    public void handleMessage(PHMessage phMessage) {
        log.info("Consumer>> " + phMessage);
        log.info("Consumer modifies a message: "+phMessage.getId());
        phMessage.setMessage(phMessage.getMessage()+" modified!");
        log.info("PHMessage saved>> " + this.phMessageRepository.save(phMessage));
    }	
}
