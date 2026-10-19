package com.duoc.centralemergencias.consumer.listener;

import com.duoc.centralemergencias.consumer.config.RabbitNames;
import com.duoc.centralemergencias.consumer.dto.Emergencia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Desafío de profundización: tercera cola/consumer, sin modificar el Producer.
 */
@Component
public class AmbulanciaConsumer {

    private static final Logger log = LoggerFactory.getLogger(AmbulanciaConsumer.class);

    @RabbitListener(queues = RabbitNames.QUEUE_AMBULANCIA)
    public void recibir(Emergencia emergencia) {
        log.info("AMBULANCIA recibió: {} | {} | {}",
                emergencia.getTipo(), emergencia.getUbicacion(), emergencia.getDescripcion());
    }
}
