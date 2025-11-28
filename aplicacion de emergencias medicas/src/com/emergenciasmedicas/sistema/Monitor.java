package com.emergenciasmedicas.sistema;

import com.emergenciasmedicas.gestores.GestorEmergencias;
import com.emergenciasmedicas.gestores.GestorRecursos;
import com.emergenciasmedicas.model.Evento;
import java.util.stream.Collectors;

// El Monitor ahora es un Suscriptor que escucha eventos.
public class Monitor implements Runnable, Suscriptor {
    private final GestorEmergencias gestorEmergencias;
    private final GestorRecursos gestorRecursos;

    public Monitor(GestorEmergencias gestorEmergencias, GestorRecursos gestorRecursos) {
        this.gestorEmergencias = gestorEmergencias;
        this.gestorRecursos = gestorRecursos;
    }

    @Override
    public void run() {
        System.out.println("Monitor de sistema iniciado y escuchando eventos.");
        // El hilo del monitor ahora solo necesita mantenerse vivo.
        // El trabajo real se hace en onEvento(), que es llamado por el hilo del CanalEventos.
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(Long.MAX_VALUE);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("El monitor fue interrumpido.");
        }
    }

    @Override
    public void onEvento(Evento evento) {
        // En lugar de imprimir cada 5 segundos, ahora imprime el estado en respuesta a un evento.
        System.out.println("\n----- ESTADO DEL SISTEMA (Evento: " + evento.getTipo() + ") -----");

        System.out.println("  >> " + evento.getMensaje());

        // Estado de las emergencias
        System.out.println("Emergencias pendientes: " + gestorEmergencias.getEmergenciasPendientes());

        // Estado de los recursos
        String estadoRecursos = gestorRecursos.getRecursosDisponibles().stream()
                .map(recurso -> "  - " + recurso.getId() + ": " + recurso.getEstado())
                .collect(Collectors.joining("\n"));
        System.out.println("Estado de los recursos:\n" + estadoRecursos);

        System.out.println("--------------------------------------------------\n");
    }
}
