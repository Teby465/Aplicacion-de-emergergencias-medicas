package com.emergenciasmedicas.model;

import com.emergenciasmedicas.util.EstadoEmergencia;
import com.emergenciasmedicas.util.Prioridad;

import java.time.LocalDateTime;

public class Emergencia implements Comparable<Emergencia> {
    private String id;
    private Prioridad prioridad;
    private LocalDateTime horaLlamada;
    private Ubicacion ubicacion;
    private EstadoEmergencia estado;

    public Emergencia(String id, Prioridad prioridad, Ubicacion ubicacion) {
        this.id = id;
        this.prioridad = prioridad;
        this.ubicacion = ubicacion;
        this.horaLlamada = LocalDateTime.now();
        this.estado = EstadoEmergencia.PENDIENTE;
    }

    @Override
    public int compareTo(Emergencia otra) {
        // Primero, compara por prioridad (CRITICO es lo más prioritario).
        int comparacionPrioridad = this.prioridad.compareTo(otra.getPrioridad());

        // Si las prioridades son las mismas, desempata por tiempo de espera.
        if (comparacionPrioridad == 0) {
            // La emergencia que llegó antes (horaLlamada menor) tiene mayor prioridad.
            return this.horaLlamada.compareTo(otra.getHoraLlamada());
        }

        return comparacionPrioridad;
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public LocalDateTime getHoraLlamada() {
        return horaLlamada;
    }

    public void setHoraLlamada(LocalDateTime horaLlamada) {
        this.horaLlamada = horaLlamada;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public EstadoEmergencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoEmergencia estado) {
        this.estado = estado;
    }
}
