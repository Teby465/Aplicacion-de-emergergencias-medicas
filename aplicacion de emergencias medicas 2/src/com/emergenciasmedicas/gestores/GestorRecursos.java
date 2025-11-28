package com.emergenciasmedicas.gestores;

import com.emergenciasmedicas.model.Ambulancia;
import com.emergenciasmedicas.model.Recurso;
import com.emergenciasmedicas.model.Ubicacion;
import com.emergenciasmedicas.util.EstadoRecurso;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GestorRecursos {
    private final List<Recurso> recursosDisponibles = new CopyOnWriteArrayList<>();
    private final Lock lock = new ReentrantLock();

    public void agregarRecurso(Recurso recurso) {
        recursosDisponibles.add(recurso);
    }

    public Ambulancia asignarAmbulanciaMasCercana(Ubicacion emergenciaUbicacion) {
        lock.lock();
        try {
            Ambulancia ambulanciaMasCercana = null;
            double distanciaMinima = Double.MAX_VALUE;

            for (Recurso recurso : recursosDisponibles) {
                if (recurso instanceof Ambulancia && recurso.getEstado() == EstadoRecurso.DISPONIBLE) {
                    Ambulancia ambulancia = (Ambulancia) recurso;
                    double distancia = ambulancia.getUbicacionActual().distanciaA(emergenciaUbicacion);
                    if (distancia < distanciaMinima) {
                        distanciaMinima = distancia;
                        ambulanciaMasCercana = ambulancia;
                    }
                }
            }

            if (ambulanciaMasCercana != null) {
                ambulanciaMasCercana.setEstado(EstadoRecurso.OCUPADO);
                return ambulanciaMasCercana;
            }

            return null; // No hay ambulancias disponibles
        } finally {
            lock.unlock();
        }
    }

    public void liberarRecurso(Recurso recurso) {
        lock.lock();
        try {
            // Solo libera el recurso si todavía existe en el sistema
            if (recursosDisponibles.contains(recurso)) {
                recurso.liberar();
            }
        } finally {
            lock.unlock();
        }
    }

    public void removerRecurso(Recurso recurso) {
        lock.lock();
        try {
            recursosDisponibles.remove(recurso);
            System.err.println("Recurso " + recurso.getId() + " removido permanentemente del sistema por fallo.");
        } finally {
            lock.unlock();
        }
    }

    public List<Recurso> getRecursosDisponibles() {
        return recursosDisponibles;
    }
}
