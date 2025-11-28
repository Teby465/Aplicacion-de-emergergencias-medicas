package com.emergenciasmedicas.gestores;

import com.emergenciasmedicas.model.Emergencia;

import java.util.concurrent.PriorityBlockingQueue;

public class GestorEmergencias {
    // PriorityBlockingQueue es una cola de prioridad segura para hilos.
    private final PriorityBlockingQueue<Emergencia> colaEmergencias = new PriorityBlockingQueue<>();

    public void agregarEmergencia(Emergencia emergencia) {
        System.out.println("Nueva emergencia recibida: " + emergencia.getId() + " con prioridad " + emergencia.getPrioridad());
        colaEmergencias.add(emergencia);
    }

    public Emergencia obtenerProximaEmergencia() throws InterruptedException {
        // El método take() bloquea hasta que haya una emergencia disponible.
        Emergencia emergencia = colaEmergencias.take();
        System.out.println("Despachando emergencia: " + emergencia.getId());
        return emergencia;
    }

    public int getEmergenciasPendientes() {
        return colaEmergencias.size();
    }
}
