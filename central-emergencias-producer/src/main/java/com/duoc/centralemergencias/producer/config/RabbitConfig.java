package com.duoc.centralemergencias.producer.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declara el exchange de forma idempotente (por si el producer arranca antes
 * que el consumer) y configura el RabbitTemplate para enviar JSON.
 * Las colas y bindings las declara el consumer, que es su dueño.
 */
@Configuration
public class RabbitConfig {

    @Bean
    public FanoutExchange emergenciasExchange() {
        return new FanoutExchange(RabbitNames.EXCHANGE_EMERGENCIAS, true, false);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
