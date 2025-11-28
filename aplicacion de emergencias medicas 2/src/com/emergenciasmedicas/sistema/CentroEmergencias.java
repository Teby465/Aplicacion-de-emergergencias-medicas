package com.emergenciasmedicas.sistema;

import com.emergenciasmedicas.actores.Despachador;
import com.emergenciasmedicas.actores.OperadorLlamadas;
import com.emergenciasmedicas.gestores.GestorEmergencias;
import com.emergenciasmedicas.gestores.GestorRecursos;
import com.emergenciasmedicas.model.Ambulancia;
import com.emergenciasmedicas.model.Ubicacion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CentroEmergencias {
    private final GestorEmergencias gestorEmergencias = new GestorEmergencias();
    private final GestorRecursos gestorRecursos = new GestorRecursos();
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private final List<Despachador> despachadores = new ArrayList<>();

    public void iniciarSimulacion() {
        System.out.println("Iniciando simulación del Centro de Emergencias Médicas.");

        CanalEventos canalEventos = CanalEventos.getInstancia();
        executor.submit(canalEventos);

        gestorRecursos.agregarRecurso(new Ambulancia("AMB-01", new Ubicacion(10, 10)));
        gestorRecursos.agregarRecurso(new Ambulancia("AMB-02", new Ubicacion(90, 90)));
        gestorRecursos.agregarRecurso(new Ambulancia("AMB-03", new Ubicacion(10, 90)));
        gestorRecursos.agregarRecurso(new Ambulancia("AMB-04", new Ubicacion(90, 10)));

        Runnable operador = new OperadorLlamadas(gestorEmergencias);
        // *** LA CORRECCIÓN ESTÁ AQUÍ ***
        // Declaramos 'monitor' como Monitor, no como Runnable.
        Monitor monitor = new Monitor(gestorEmergencias, gestorRecursos);

        // Ahora la suscripción es directa, sin cast.
        canalEventos.suscribir(monitor);

        Despachador despachador1 = new Despachador("Alfa", gestorEmergencias, gestorRecursos);
        Despachador despachador2 = new Despachador("Bravo", gestorEmergencias, gestorRecursos);
        despachadores.add(despachador1);
        despachadores.add(despachador2);

        executor.submit(operador);
        executor.submit(despachador1);
        executor.submit(despachador2);
        // 'monitor' sigue siendo un Runnable válido para el executor.
        executor.submit(monitor);
    }

    public void detenerSimulacion() throws InterruptedException {
        System.out.println("Deteniendo la simulación...");
        executor.shutdownNow();

        for (Despachador d : despachadores) {
            d.shutdown();
        }

        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
            System.err.println("Los hilos principales no terminaron a tiempo.");
        }

        System.out.println("Simulación detenida.");
    }
}
