package com.superpet.ProyectoSuperpet.dto;

public class AccionDTO {
    private String texto;
    private String url;

    public AccionDTO(String texto, String url) {
        this.texto = texto;
        this.url = url;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}