package com.duoc.centralemergencias.consumer.listener;

import com.duoc.centralemergencias.consumer.config.RabbitNames;
import com.duoc.centralemergencias.consumer.dto.Emergencia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PoliciaConsumer {

    private static final Logger log = LoggerFactory.getLogger(PoliciaConsumer.class);

    @RabbitListener(queues = RabbitNames.QUEUE_POLICIA)
    public void recibir(Emergencia emergencia) {
        log.info("POLICÍA recibió: {} | {} | {}",
                emergencia.getTipo(), emergencia.getUbicacion(), emergencia.getDescripcion());
    }
}
