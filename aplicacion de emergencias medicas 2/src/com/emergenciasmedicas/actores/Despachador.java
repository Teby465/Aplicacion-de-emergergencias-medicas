package com.emergenciasmedicas.actores;

import com.emergenciasmedicas.gestores.GestorEmergencias;
import com.emergenciasmedicas.gestores.GestorRecursos;
import com.emergenciasmedicas.model.Ambulancia;
import com.emergenciasmedicas.model.Emergencia;
import com.emergenciasmedicas.model.Evento;
import com.emergenciasmedicas.sistema.CanalEventos;
import com.emergenciasmedicas.util.EstadoEmergencia;
import com.emergenciasmedicas.util.TipoEvento;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Despachador implements Runnable {
    private final String nombre;
    private final GestorEmergencias gestorEmergencias;
    private final GestorRecursos gestorRecursos;
    private final ExecutorService poolAmbulancias = Executors.newCachedThreadPool();

    public Despachador(String nombre, GestorEmergencias gestorEmergencias, GestorRecursos gestorRecursos) {
        this.nombre = nombre;
        this.gestorEmergencias = gestorEmergencias;
        this.gestorRecursos = gestorRecursos;
    }

    @Override
    public void run() {
        System.out.println("Despachador [" + nombre + "] iniciado.");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Emergencia emergencia = gestorEmergencias.obtenerProximaEmergencia();
                emergencia.setEstado(EstadoEmergencia.ASIGNADA);

                Ambulancia ambulanciaAsignada = null;
                while (ambulanciaAsignada == null) {
                    ambulanciaAsignada = gestorRecursos.asignarAmbulanciaMasCercana(emergencia.getUbicacion());
                    if (ambulanciaAsignada == null) {
                        TimeUnit.SECONDS.sleep(2);
                    }
                }

                String msg = String.format("Despachador [%s] asignó ambulancia %s a emergencia %s", nombre, ambulanciaAsignada.getId(), emergencia.getId());
                CanalEventos.getInstancia().publicar(new Evento(TipoEvento.EMERGENCIA_ASIGNADA, msg));

                ambulanciaAsignada.asignarEmergencia(emergencia);

                // Pasa el gestor de emergencias al hilo de la ambulancia para la re-planificación
                poolAmbulancias.execute(new AmbulanciaThread(ambulanciaAsignada, gestorRecursos, gestorEmergencias));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("El despachador [" + nombre + "] fue interrumpido.");
        } finally {
            poolAmbulancias.shutdown();
        }
        System.out.println("Despachador [" + nombre + "] ha terminado su turno.");
    }

    public void shutdown() {
        poolAmbulancias.shutdown();
        try {
            if (!poolAmbulancias.awaitTermination(5, TimeUnit.SECONDS)) {
                poolAmbulancias.shutdownNow();
            }
        } catch (InterruptedException e) {
            poolAmbulancias.shutdownNow();
        }
    }
}
