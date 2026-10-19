package com.duoc.centralemergencias.consumer.dto;

/**
 * Representa el mensaje recibido desde emergencias.exchange.
 */
public class Emergencia {

    private String tipo;
    private String ubicacion;
    private String descripcion;

    public Emergencia() {
    }

    public Emergencia(String tipo, String ubicacion, String descripcion) {
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
