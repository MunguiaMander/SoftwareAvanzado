package com.mander.teplatemethodpattern.model;

public record Contenedor(String nombre, String imagen, String host, int puerto) {

    public boolean expuestoAInternet() {
        return "0.0.0.0".equals(host);
    }
}
