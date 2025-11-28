package com.emergenciasmedicas.sistema;

import com.emergenciasmedicas.model.Evento;

@FunctionalInterface
public interface Suscriptor {
    void onEvento(Evento evento);
}
