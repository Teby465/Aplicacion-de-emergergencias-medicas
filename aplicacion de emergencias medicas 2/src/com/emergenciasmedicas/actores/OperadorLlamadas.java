package com.emergenciasmedicas.actores;

import com.emergenciasmedicas.gestores.GestorEmergencias;
import com.emergenciasmedicas.model.Emergencia;
import com.emergenciasmedicas.model.Evento;
import com.emergenciasmedicas.model.Ubicacion;
import com.emergenciasmedicas.sistema.CanalEventos;
import com.emergenciasmedicas.util.Prioridad;
import com.emergenciasmedicas.util.TipoEvento;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OperadorLlamadas implements Runnable {
    private final GestorEmergencias gestorEmergencias;
    private final Random random = new Random();

    public OperadorLlamadas(GestorEmergencias gestorEmergencias) {
        this.gestorEmergencias = gestorEmergencias;
    }

    @Override
    public void run() {
        System.out.println("Operador de llamadas iniciado.");
        try {
            for (int i = 0; i < 10; i++) {
                TimeUnit.SECONDS.sleep(random.nextInt(3) + 1);

                String id = "EMG-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
                Prioridad prioridad = Prioridad.values()[random.nextInt(Prioridad.values().length)];
                Ubicacion ubicacion = new Ubicacion(random.nextDouble() * 100, random.nextDouble() * 100);
                Emergencia nuevaEmergencia = new Emergencia(id, prioridad, ubicacion);

                // Publica el evento antes de añadirla a la cola
                CanalEventos.getInstancia().publicar(new Evento(TipoEvento.NUEVA_EMERGENCIA, "Nueva emergencia recibida: " + id + " con prioridad " + prioridad));

                gestorEmergencias.agregarEmergencia(nuevaEmergencia);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("El operador de llamadas fue interrumpido.");
        }
        System.out.println("Operador de llamadas ha terminado su turno.");
    }
}
