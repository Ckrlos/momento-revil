package com.duoc.centralemergencias.consumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Dueño de la topología de RabbitMQ: declara el fanout exchange, las tres
 * colas durables (policia, bomberos, ambulancia) y sus bindings. Al arrancar
 * el consumer quedan creadas y vinculadas, aunque el Producer no esté activo.
 * El MessageConverter declarado aquí es detectado automáticamente por Spring
 * Boot y usado por el listener container factory de @RabbitListener.
 */
@Configuration
public class RabbitConfig {

    @Bean
    public FanoutExchange emergenciasExchange() {
        return new FanoutExchange(RabbitNames.EXCHANGE_EMERGENCIAS, true, false);
    }

    @Bean
    public Queue colaPolicia() {
        return new Queue(RabbitNames.QUEUE_POLICIA, true);
    }

    @Bean
    public Queue colaBomberos() {
        return new Queue(RabbitNames.QUEUE_BOMBEROS, true);
    }

    @Bean
    public Queue colaAmbulancia() {
        return new Queue(RabbitNames.QUEUE_AMBULANCIA, true);
    }

    @Bean
    public Binding bindingPolicia(Queue colaPolicia, FanoutExchange emergenciasExchange) {
        return BindingBuilder.bind(colaPolicia).to(emergenciasExchange);
    }

    @Bean
    public Binding bindingBomberos(Queue colaBomberos, FanoutExchange emergenciasExchange) {
        return BindingBuilder.bind(colaBomberos).to(emergenciasExchange);
    }

    @Bean
    public Binding bindingAmbulancia(Queue colaAmbulancia, FanoutExchange emergenciasExchange) {
        return BindingBuilder.bind(colaAmbulancia).to(emergenciasExchange);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
