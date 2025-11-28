package com.emergenciasmedicas.model;

import com.emergenciasmedicas.util.EstadoRecurso;

public class PersonalMedico implements Recurso {
    private String id;
    private String nombre;
    private String especialidad;
    private EstadoRecurso estado;

    public PersonalMedico(String id, String nombre, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
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
        this.estado = EstadoRecurso.OCUPADO;
    }

    @Override
    public void liberar() {
        this.estado = EstadoRecurso.DISPONIBLE;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }
}
