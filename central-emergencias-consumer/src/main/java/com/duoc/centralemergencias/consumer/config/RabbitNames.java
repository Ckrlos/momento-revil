package com.duoc.centralemergencias.consumer.config;

/**
 * Nombres centralizados de los recursos de RabbitMQ usados por el consumer.
 * Evita strings repetidos ("mágicos") en los listeners.
 */
public final class RabbitNames {

    public static final String EXCHANGE_EMERGENCIAS = "emergencias.exchange";

    public static final String QUEUE_POLICIA = "emergencias.policia";
    public static final String QUEUE_BOMBEROS = "emergencias.bomberos";
    public static final String QUEUE_AMBULANCIA = "emergencias.ambulancia";

    private RabbitNames() {
    }
}
