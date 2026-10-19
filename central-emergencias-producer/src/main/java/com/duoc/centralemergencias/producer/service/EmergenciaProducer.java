package com.duoc.centralemergencias.producer.service;

import com.duoc.centralemergencias.producer.config.RabbitNames;
import com.duoc.centralemergencias.producer.dto.EmergenciaRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Publica emergencias en el fanout exchange. La routing key se deja vacía
 * porque un fanout la ignora: entrega una copia a cada cola vinculada.
 */
@Service
public class EmergenciaProducer {

    private static final Logger log = LoggerFactory.getLogger(EmergenciaProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public EmergenciaProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicar(EmergenciaRequest emergencia) {
        rabbitTemplate.convertAndSend(RabbitNames.EXCHANGE_EMERGENCIAS, "", emergencia);
        log.info("Emergencia publicada en {}: {}", RabbitNames.EXCHANGE_EMERGENCIAS, emergencia);
    }
}
