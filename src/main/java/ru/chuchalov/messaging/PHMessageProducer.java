package ru.chuchalov.messaging;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import ru.chuchalov.messaging.PHMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PHMessageProducer {
	private static Logger log = LoggerFactory.getLogger(PHMessageProducer.class);
	private RedisTemplate redisTemplate;
	
	public PHMessageProducer(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	public void sendTo(String topic, PHMessage phMessage) {
		log.info("PHMessage>> sent: "+phMessage.getId());
		redisTemplate.convertAndSend(topic, phMessage);
	}
}
