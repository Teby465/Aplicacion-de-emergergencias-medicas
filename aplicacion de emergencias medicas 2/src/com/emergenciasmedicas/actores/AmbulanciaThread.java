package com.emergenciasmedicas.actores;

import com.emergenciasmedicas.gestores.GestorEmergencias;
import com.emergenciasmedicas.gestores.GestorRecursos;
import com.emergenciasmedicas.model.Ambulancia;
import com.emergenciasmedicas.model.Emergencia;
import com.emergenciasmedicas.model.Evento;
import com.emergenciasmedicas.sistema.CanalEventos;
import com.emergenciasmedicas.util.EstadoEmergencia;
import com.emergenciasmedicas.util.EstadoRecurso;
import com.emergenciasmedicas.util.TipoEvento;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AmbulanciaThread implements Runnable {
    private final Ambulancia ambulancia;
    private final GestorRecursos gestorRecursos;
    private final GestorEmergencias gestorEmergencias;
    private final Random random = new Random();

    public AmbulanciaThread(Ambulancia ambulancia, GestorRecursos gestorRecursos, GestorEmergencias gestorEmergencias) {
        this.ambulancia = ambulancia;
        this.gestorRecursos = gestorRecursos;
        this.gestorEmergencias = gestorEmergencias;
    }

    @Override
    public void run() {
        Emergencia emergencia = ambulancia.getEmergenciaAsignada();
        if (emergencia == null) return;

        CanalEventos canal = CanalEventos.getInstancia();
        boolean misionCompletada = false;

        try {
            emergencia.setEstado(EstadoEmergencia.EN_CAMINO);
            ambulancia.setEstado(EstadoRecurso.EN_CAMINO);
            canal.publicar(new Evento(TipoEvento.AMBULANCIA_EN_CAMINO, "Ambulancia " + ambulancia.getId() + " en ruta a emergencia " + emergencia.getId()));
            TimeUnit.SECONDS.sleep(5);

            if (random.nextInt(100) < 15) {
                throw new RuntimeException("Fallo mecánico de la ambulancia " + ambulancia.getId());
            }

            emergencia.setEstado(EstadoEmergencia.ATENDIDA);
            canal.publicar(new Evento(TipoEvento.EMERGENCIA_ATENDIDA, "Ambulancia " + ambulancia.getId() + " atendiendo emergencia " + emergencia.getId()));
            TimeUnit.SECONDS.sleep(3);

            emergencia.setEstado(EstadoEmergencia.CERRADA);
            misionCompletada = true;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            replanificarEmergencia(emergencia, "Servicio de ambulancia " + ambulancia.getId() + " interrumpido.");
            canal.publicar(new Evento(TipoEvento.FALLO_RECURSO, "Servicio interrumpido para ambulancia " + ambulancia.getId()));
        } catch (RuntimeException fallo) {
            ambulancia.setEstado(EstadoRecurso.NO_DISPONIBLE);
            gestorRecursos.removerRecurso(ambulancia);
            replanificarEmergencia(emergencia, fallo.getMessage());
            canal.publicar(new Evento(TipoEvento.FALLO_RECURSO, fallo.getMessage()));
        } finally {
            if (misionCompletada) {
                gestorRecursos.liberarRecurso(ambulancia);
                canal.publicar(new Evento(TipoEvento.RECURSO_LIBERADO, "Ambulancia " + ambulancia.getId() + " ahora está DISPONIBLE."));
            }
        }
    }

    private void replanificarEmergencia(Emergencia emergencia, String motivo) {
        System.err.println("¡FALLO! " + motivo + ". Re-planificando emergencia " + emergencia.getId());
        emergencia.setEstado(EstadoEmergencia.FALLIDA);
        gestorEmergencias.agregarEmergencia(emergencia);
    }
}
