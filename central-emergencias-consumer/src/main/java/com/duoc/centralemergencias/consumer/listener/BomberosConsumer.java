package com.duoc.centralemergencias.consumer.listener;

import com.duoc.centralemergencias.consumer.config.RabbitNames;
import com.duoc.centralemergencias.consumer.dto.Emergencia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BomberosConsumer {

    private static final Logger log = LoggerFactory.getLogger(BomberosConsumer.class);

    @RabbitListener(queues = RabbitNames.QUEUE_BOMBEROS)
    public void recibir(Emergencia emergencia) {
        log.info("BOMBEROS recibió: {} | {} | {}",
                emergencia.getTipo(), emergencia.getUbicacion(), emergencia.getDescripcion());
    }
}
