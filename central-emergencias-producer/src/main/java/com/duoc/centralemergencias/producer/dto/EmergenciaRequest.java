package com.duoc.centralemergencias.producer.dto;

/**
 * Cuerpo JSON que recibe POST /emergencias.
 */
public class EmergenciaRequest {

    private String tipo;
    private String ubicacion;
    private String descripcion;

    public EmergenciaRequest() {
    }

    public EmergenciaRequest(String tipo, String ubicacion, String descripcion) {
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

    @Override
    public String toString() {
        return "EmergenciaRequest{tipo='" + tipo + "', ubicacion='" + ubicacion
                + "', descripcion='" + descripcion + "'}";
    }
}
