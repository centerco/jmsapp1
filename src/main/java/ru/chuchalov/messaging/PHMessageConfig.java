package ru.chuchalov.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * Конфигурационный класс для Spring Boot. Осуществляет создание сущностей Слушателя, Адаптера и Шаблона для Redis-server
 * 
 * @author Andrei Chuchalov
 * @version 1.0
 */
@Configuration
public class PHMessageConfig {
	
	/**
	 * Контейнер для фабрики соединений с Redis-server
	 * @param connectionFactory фабрика соединений с Redis-server
	 * @param phMessageListenerAdapter адаптер подписчика 
	 * @param topic название очереди на сервере
	 * @return ссылку на контейнер
	 */
	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter phMessageListenerAdapter, @Value("${pmh.redis.topic}") String topic) {
		
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(phMessageListenerAdapter, new PatternTopic(topic));
		return container;
	}
	
	/**
	 * Сущность определяет класс {@link PHMessageConsumer} в качестве подписчика на сообщения в очереди Redis-server
	 * @param consumer экземпляр {@link PHMessageConsumer}
	 * @return ссылка на адаптер
	 */
	@Bean
	MessageListenerAdapter phMessageListenerAdapter(PHMessageConsumer consumer) {
		
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(consumer);
		messageListenerAdapter.setSerializer(new Jackson2JsonRedisSerializer<>(PHMessage.class));
		return messageListenerAdapter;
	}

	/**
	 * Сущность определяет шаблон обмена сообщениями с Redis-server
	 * @param connectionFactory ссылка на фабрику соеднинений Redis-server
	 * @return ссылка на объект
	 */
	@Bean
	RedisTemplate<String, PHMessage> redisTemplate(RedisConnectionFactory connectionFactory) {
		
		RedisTemplate<String, PHMessage> redisTemplate = new RedisTemplate<String, PHMessage>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setDefaultSerializer(new Jackson2JsonRedisSerializer<>(PHMessage.class));
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
}
