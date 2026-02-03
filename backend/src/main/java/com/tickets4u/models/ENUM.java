package com.tickets4u.models;

import java.lang.Override;

public enum ENUM {
    ACTIVO("activo"),
    USADO("usado"),
    CANCELADO("cancelado"),
    DESCONOCIDO("desconocido");

    private final String valor;

    ENUM(String valor) {
        this.valor = valor;
    }

    @Override  
    public String toString() {
        return this.valor;
    }

    public static ENUM fromString(String valor) {
        if (valor == null) return DESCONOCIDO;
        for (ENUM estado : ENUM.values()) {
            if (estado.valor.equalsIgnoreCase(valor)) {
                return estado;
            }
        }
        return DESCONOCIDO;
    }
}
