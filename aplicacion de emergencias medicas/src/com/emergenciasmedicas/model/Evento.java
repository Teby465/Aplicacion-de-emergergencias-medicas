package com.emergenciasmedicas.model;

import com.emergenciasmedicas.util.TipoEvento;

import java.time.LocalDateTime;

public class Evento {
    private final TipoEvento tipo;
    private final String mensaje;
    private final LocalDateTime timestamp;

    public Evento(TipoEvento tipo, String mensaje) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.timestamp = LocalDateTime.now();
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] [" + tipo + "] " + mensaje;
    }
}
