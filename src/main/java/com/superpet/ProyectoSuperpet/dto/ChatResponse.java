package com.superpet.ProyectoSuperpet.dto;

public class ChatResponse {
    private String mensaje;
    private AccionDTO accion;

    public ChatResponse(String mensaje, AccionDTO accion) {
        this.mensaje = mensaje;
        this.accion = accion;
    }

    
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public AccionDTO getAccion() {
        return accion;
    }

    public void setAccion(AccionDTO accion) {
        this.accion = accion;
    }
}