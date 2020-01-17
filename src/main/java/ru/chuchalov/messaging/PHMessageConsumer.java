package ru.chuchalov.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PHMessageConsumer {
	
    private static final Logger log = LoggerFactory.getLogger(PHMessageConsumer.class);
    private PHMessageRepository phMessageRepository;
    
    public PHMessageConsumer(PHMessageRepository phMessageRepository){
        this.phMessageRepository = phMessageRepository;
    }
    
    public void handleMessage(PHMessage phMessage) {
        log.info("Consumer>> " + phMessage);
        log.info("Consumer modifies a message: "+phMessage.getId());
        phMessage.setMessage(phMessage.getMessage()+" modified!");
        log.info("PHMessage saved>> " + this.phMessageRepository.save(phMessage));
    }	
}
