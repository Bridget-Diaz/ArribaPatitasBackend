package com.superpet.ProyectoSuperpet.dto;

import java.util.List;

public class GeminiIntentResponse {

    private String intent; 
    private String keyword; 
    private Integer cantidad; 
    private List<ItemDTO> items; // pedidos complejos por el chatbot wa

    public static class ItemDTO {
        private String producto;
        private Integer cantidad;

        public String getProducto() { return producto; }
        public void setProducto(String producto) { this.producto = producto; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }

    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public List<ItemDTO> getItems() { return items; }
    public void setItems(List<ItemDTO> items) { this.items = items; }
}
