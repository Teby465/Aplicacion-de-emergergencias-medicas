package com.emergenciasmedicas.model;

import com.emergenciasmedicas.util.EstadoRecurso;

public interface Recurso {
    String getId();
    EstadoRecurso getEstado();
    void setEstado(EstadoRecurso estado);
    void asignarEmergencia(Emergencia emergencia);
    void liberar();
}
