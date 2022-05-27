package com.notelify.enums;

/*
    Definición de estados por los que pasarán las tareas.
**/
public enum Estado {
    TODO("Para hacer"), IN_PROGRESS("En progreso"), FINISHED("Finalizada"), PAUSED("En pausa");

    private final String valor;

    private Estado(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
