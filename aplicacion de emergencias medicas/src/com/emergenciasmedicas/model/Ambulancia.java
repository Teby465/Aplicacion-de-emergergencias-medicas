package com.emergenciasmedicas.model;

import com.emergenciasmedicas.util.EstadoRecurso;

public class Ambulancia implements Recurso {
    private final String id;
    private final Ubicacion base;
    private Ubicacion ubicacionActual;
    private EstadoRecurso estado;
    private Emergencia emergenciaAsignada;

    public Ambulancia(String id, Ubicacion base) {
        this.id = id;
        this.base = base;
        this.ubicacionActual = base; // Comienza en su base
        this.estado = EstadoRecurso.DISPONIBLE;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public EstadoRecurso getEstado() {
        return estado;
    }

    @Override
    public void setEstado(EstadoRecurso estado) {
        this.estado = estado;
    }

    @Override
    public void asignarEmergencia(Emergencia emergencia) {
        this.emergenciaAsignada = emergencia;
        this.estado = EstadoRecurso.OCUPADO;
    }

    @Override
    public void liberar() {
        this.emergenciaAsignada = null;
        this.estado = EstadoRecurso.DISPONIBLE;
        this.ubicacionActual = this.base; // Regresa a la base
        System.out.println("Ambulancia " + id + " ha regresado a la base en " + base.getLatitud() + "," + base.getLongitud());
    }

    public Emergencia getEmergenciaAsignada() {
        return emergenciaAsignada;
    }

    public Ubicacion getUbicacionActual() {
        return ubicacionActual;
    }

    public void setUbicacionActual(Ubicacion ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }
}
