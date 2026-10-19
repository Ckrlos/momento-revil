package com.duoc.centralemergencias.producer.config;

/**
 * Nombres centralizados de los recursos de RabbitMQ usados por el producer.
 * Evita strings repetidos ("mágicos") en controladores y servicios.
 */
public final class RabbitNames {

    public static final String EXCHANGE_EMERGENCIAS = "emergencias.exchange";

    private RabbitNames() {
    }
}
