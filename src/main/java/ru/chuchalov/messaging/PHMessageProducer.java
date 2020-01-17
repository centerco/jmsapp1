package ru.chuchalov.messaging;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import ru.chuchalov.messaging.PHMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс отвечает за отправку сообщений {@link PHMessage} в очередь Redis-server
 * 
 * @author Andrei Chuchalov
 * @version 1.0
 *
 */
@Component
public class PHMessageProducer {
	private static Logger log = LoggerFactory.getLogger(PHMessageProducer.class);
	private RedisTemplate redisTemplate;
	
	/**
	 * Конструктор сохраняет ссылку на экземпляр шаблона отправки сообщений на Redis-server
	 * @param redisTemplate объект класса шаблона
	 */
	public PHMessageProducer(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	/**
	 * Метод осуществляет отправку сообщения в очередь Redis-server
	 * @param topic название очереди
	 * @param phMessage экземпляр сообщения
	 */
	public void sendTo(String topic, PHMessage phMessage) {
		log.info("PHMessage sent to "+topic+">>: "+phMessage.getId());
		redisTemplate.convertAndSend(topic, phMessage);
	}
}
