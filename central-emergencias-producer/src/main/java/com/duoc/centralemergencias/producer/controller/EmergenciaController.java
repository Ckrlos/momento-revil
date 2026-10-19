package com.duoc.centralemergencias.producer.controller;

import com.duoc.centralemergencias.producer.dto.EmergenciaRequest;
import com.duoc.centralemergencias.producer.service.EmergenciaProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/emergencias")
public class EmergenciaController {

    private final EmergenciaProducer emergenciaProducer;

    public EmergenciaController(EmergenciaProducer emergenciaProducer) {
        this.emergenciaProducer = emergenciaProducer;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> recibirEmergencia(@RequestBody EmergenciaRequest request) {
        emergenciaProducer.publicar(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of(
                        "status", "publicado",
                        "mensaje", "Emergencia recibida y publicada en RabbitMQ"
                ));
    }
}
