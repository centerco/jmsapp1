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

@Configuration
public class PHMessageConfig {
	
	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter toDoListenerAdapter, @Value("${pmh.redis.topic}") String topic) {
		
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(toDoListenerAdapter, new PatternTopic(topic));
		return container;
	}
	
	@Bean
	MessageListenerAdapter phMessageListenerAdapter(PHMessageConsumer consumer) {
		
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(consumer);
		messageListenerAdapter.setSerializer(new Jackson2JsonRedisSerializer<>(PHMessage.class));
		return messageListenerAdapter;
	}

	@Bean
	RedisTemplate<String, PHMessage> redisTemplate(RedisConnectionFactory connectionFactory) {
		
		RedisTemplate<String, PHMessage> redisTemplate = new RedisTemplate<String, PHMessage>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setDefaultSerializer(new Jackson2JsonRedisSerializer<>(PHMessage.class));
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
}
